package br.com.inseguros.ui.signup

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.preference.PreferenceManager
import br.com.concrete.canarinho.watcher.MascaraNumericaTextWatcher
import br.com.in_seguros_utils.makeErrorShortToast
import br.com.inseguros.R
import br.com.inseguros.data.AppSession
import br.com.inseguros.data.UserSession
import br.com.inseguros.data.enums.SaveStatusEnum
import br.com.inseguros.data.model.User
import br.com.inseguros.data.utils.Constants
import br.com.inseguros.databinding.SignUpFragmentBinding
import br.com.inseguros.ui.BaseFragment
import br.com.inseguros.utils.validMaterialEditTextFilled
import org.koin.android.viewmodel.ext.android.viewModel

class SignUpFragment : BaseFragment() {

    override val layout = R.layout.sign_up_fragment
    private var checkBoxDone = false
    private lateinit var mContext: Context
    private lateinit var binding: SignUpFragmentBinding
    private lateinit var navController: NavController
    private val mViewModel: SignUpViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = SignUpFragmentBinding.inflate(layoutInflater)
        activity?.setContentView(binding.root)
        navController = Navigation.findNavController(view)

        this.setupListeners()
        this.setupObservers()
        this.checkCachedFields()
        this.setupFieldsFormat()

    }

    private fun setupListeners() {

        binding.cancelRegisterBtn.setOnClickListener {
            navController.popBackStack()
        }

        binding.createRegisterBtn.setOnClickListener {
            if (!checkBoxDone) {
                getString(R.string.read_use_terms_msg).makeErrorShortToast(mContext)

            } else {
                if (validateFieldsFilled()) {
                    setupContainersVisible(false)
                    val prefs = PreferenceManager.getDefaultSharedPreferences(mContext)
                    val newToken = prefs.getString(Constants.NEW_TOKEN_KEY, "")
                    if (!newToken.isNullOrEmpty()) {
                        mViewModel.signUp(
                            User(
                                displayName = binding.userRegisterMet.text.toString(),
                                userLogin = binding.emailRegisterMet.text.toString(),
                                phone = binding.phoneNumberRegisterMet.text.toString(),
                                messagingService = newToken,
                                passWD = binding.passwdRegisterMet.text.toString()
                            )
                        )
                    } else {
                        "Err-ntn01 - Erro Interno".makeErrorShortToast(mContext)
                    }
                }
            }
        }

        binding.useTermRegisterContainer.setOnClickListener {
            cacheFilledFields()
            navController.navigate(R.id.action_signUpFragment_to_useTermFragment)
        }
        binding.useTermsAcceptLa.setOnClickListener {
            cacheFilledFields()
            navController.navigate(R.id.action_signUpFragment_to_useTermFragment)
        }
    }

    private fun setupObservers() {

        mViewModel.getSignUpStatus().observe(viewLifecycleOwner, object : Observer<SaveStatusEnum> {
            override fun onChanged(saveStatus: SaveStatusEnum?) {
                if (saveStatus == SaveStatusEnum.SUCCESS) {
                    navController.navigate(R.id.action_signUpFragment_to_navigation_home)
                } else {
                    getString(R.string.register_error_msg).makeErrorShortToast(mContext)
                }
                setupContainersVisible(true)
                mViewModel.getSignUpStatus().removeObserver(this)
            }
        })
    }

    private fun setUseTermState(state: Boolean) {
        if (state) {
            binding.useTermsAcceptLa.speed = 1f
            binding.useTermsAcceptLa.playAnimation()
            checkBoxDone = true
        } else if (checkBoxDone) {
            binding.useTermsAcceptLa.speed = -1f
            binding.useTermsAcceptLa.playAnimation()
            checkBoxDone = false
        }
    }

    override fun onResume() {
        super.onResume()
        setUseTermState(UserSession.getUseTermSignUpState())
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    private fun validateFieldsFilled(): Boolean {

        val errors = arrayListOf<String>()

        if (!validMaterialEditTextFilled(binding.userRegisterMet, mContext))
            errors.add("User Name")
        if (!validMaterialEditTextFilled(binding.emailRegisterMet, mContext))
            errors.add("User Email")
        if (!validMaterialEditTextFilled(binding.phoneNumberRegisterMet, mContext))
            errors.add("User Phone")
        if (!validMaterialEditTextFilled(binding.passwdRegisterMet, mContext))
            errors.add("User Passwd")

        return errors.isEmpty()
    }

    private fun setupContainersVisible(contentContainerVisible: Boolean) {

        if (contentContainerVisible) {
            binding.signUpContainer.visibility = View.VISIBLE
            binding.loadingContainer.visibility = View.GONE
        } else {
            binding.signUpContainer.visibility = View.GONE
            binding.loadingContainer.visibility = View.VISIBLE
        }
    }

    private fun checkCachedFields() {

        val userRegisterMetContent = AppSession.getCachedValue("userRegisterMet")
        if (userRegisterMetContent != null && userRegisterMetContent is String) {
            binding.userRegisterMet.setText(userRegisterMetContent)
            AppSession.removeCachedValue("userRegisterMet")
        }

        val emailRegisterMetContent = AppSession.getCachedValue("emailRegisterMet")
        if (emailRegisterMetContent != null && emailRegisterMetContent is String) {
            binding.emailRegisterMet.setText(emailRegisterMetContent)
            AppSession.removeCachedValue("emailRegisterMet")
        }

        val phoneNumberRegisterMetContent = AppSession.getCachedValue("phoneNumberRegisterMet")
        if (phoneNumberRegisterMetContent != null && phoneNumberRegisterMetContent is String) {
            binding.phoneNumberRegisterMet.setText(phoneNumberRegisterMetContent)
            AppSession.removeCachedValue("phoneNumberRegisterMet")
        }

        val passwdRegisterMetContent = AppSession.getCachedValue("passwdRegisterMet")
        if (passwdRegisterMetContent != null && passwdRegisterMetContent is String) {
            binding.passwdRegisterMet.setText(passwdRegisterMetContent)
            AppSession.removeCachedValue("passwdRegisterMet")
        }
    }

    private fun cacheFilledFields() {

        val userRegisterMetContent = binding.userRegisterMet.text.toString()
        if (userRegisterMetContent.isNotEmpty())
            AppSession.setCacheValue("userRegisterMet", userRegisterMetContent)

        val emailRegisterMetContent = binding.emailRegisterMet.text.toString()
        if (emailRegisterMetContent.isNotEmpty())
            AppSession.setCacheValue("emailRegisterMet", emailRegisterMetContent)

        val phoneNumberRegisterMetContent = binding.phoneNumberRegisterMet.text.toString()
        if (phoneNumberRegisterMetContent.isNotEmpty())
            AppSession.setCacheValue("phoneNumberRegisterMet", phoneNumberRegisterMetContent)

        val passwdRegisterMetContent = binding.passwdRegisterMet.text.toString()
        if (passwdRegisterMetContent.isNotEmpty())
            AppSession.setCacheValue("passwdRegisterMet", passwdRegisterMetContent)
    }

    private fun setupFieldsFormat() {

        // Phone
        binding.phoneNumberRegisterMet
            .addTextChangedListener(MascaraNumericaTextWatcher("(##) #####-####"))
    }

}