package br.com.inseguros.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.preference.PreferenceManager
import br.com.inseguros.R
import br.com.inseguros.data.UserSession
import br.com.inseguros.data.model.User
import br.com.inseguros.databinding.LoginFragmentBinding
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
            showLoading()
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
                mViewModel.signInWithEmailAndPassword(
                    User(
                        userLogin =  binding.userLoginMet.text.toString(),
                        passWD =  binding.passwdLoginMet.text.toString()
                    )
                )
            }
        }
    }

    private fun setupObservers() {

        mViewModel.getCurrentUserAuthLiveData().observe(viewLifecycleOwner, object : Observer<User> {
            override fun onChanged(user: User) {
                if (user.userID.isNotEmpty()) {
                    UserSession.setUserName(user.displayName)
                    UserSession.setUserEmail(user.userLogin)
                    navController.navigate(R.id.action_loginFragment_to_navigation_home)
                    hideLoading()
                    mViewModel.getCurrentUserAuthLiveData().removeObserver(this)
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

}