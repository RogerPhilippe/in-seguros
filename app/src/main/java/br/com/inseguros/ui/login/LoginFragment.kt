package br.com.inseguros.ui.login

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.preference.PreferenceManager
import br.com.in_seguros_utils.makeErrorShortToast
import br.com.in_seguros_utils.makeShortToast
import br.com.inseguros.R
import br.com.inseguros.data.sessions.UserSession
import br.com.inseguros.data.model.User
import br.com.inseguros.databinding.LoginFragmentBinding
import br.com.inseguros.ui.BaseFragment
import br.com.inseguros.utils.validMaterialEditTextFilled
import org.koin.android.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment() {

    override val layout = R.layout.login_fragment
    private lateinit var binding: LoginFragmentBinding
    private lateinit var mContext: Context
    private lateinit var navController: NavController
    private var autoLogin = false
    private val mViewModel: LoginViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = LoginFragmentBinding.inflate(layoutInflater)
        activity?.setContentView(binding.root)
        navController = Navigation.findNavController(view)

        /**
         * auto_login
         * notification_active
         * app_desc
         * app_version
         */
        val prefs = PreferenceManager.getDefaultSharedPreferences(mContext)
        autoLogin = prefs.getBoolean("auto_login", false)

        if (autoLogin) {
            setupContainersView(false)
            mViewModel.checkUserLogged()
        } else {
            mViewModel.logoutCurrentUser()
        }

        setupListeners()
        setupObservers()

    }

    private fun setupListeners() {

        binding.enterLoginBtn.setOnClickListener {

            if (validateFieldsFilled()) {
                setupContainersView(false)
                mViewModel.signInWithEmailAndPassword(
                    User(
                        userLogin =  binding.userLoginMet.text.toString(),
                        passWD =  binding.passwdLoginMet.text.toString()
                    )
                )
            }
        }

        binding.registerTvBtn.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_signUpFragment)
        }

        binding.forgotTvBtn.setOnClickListener {

            if (binding.userLoginMet.text.toString().isEmpty())
                getString(R.string.please_insert_email).makeShortToast(requireContext())
            else
                mViewModel.forgotPassword(binding.userLoginMet.text.toString())
        }
    }

    private fun setupObservers() {

        mViewModel.getCurrentUserAuthLiveData().observe(viewLifecycleOwner, object : Observer<User> {
            override fun onChanged(user: User) {
                if (user.userID.isNotEmpty()) {
                    UserSession.fillUser(user)
                    navController.navigate(R.id.action_loginFragment_to_navigation_home)
                    mViewModel.getCurrentUserAuthLiveData().removeObserver(this)
                } else {
                    getString(R.string.login_error).makeErrorShortToast(mContext)
                    setupContainersView(true)
                }
            }
        })
    }

    private fun validateFieldsFilled(): Boolean {

        val errors = arrayListOf<String>()

        if (!validMaterialEditTextFilled(binding.userLoginMet, mContext))
            errors.add("User Login")
        if (!validMaterialEditTextFilled(binding.passwdLoginMet, mContext))
            errors.add("User Passwd")

        return errors.isEmpty()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    private fun setupContainersView(contentContainerVisible: Boolean) {

        if (contentContainerVisible) {
            binding.loadingContainer.visibility = View.GONE
            binding.loginContainer.visibility = View.VISIBLE
        } else {
            binding.loadingContainer.visibility = View.VISIBLE
            binding.loginContainer.visibility = View.GONE
        }
    }

}