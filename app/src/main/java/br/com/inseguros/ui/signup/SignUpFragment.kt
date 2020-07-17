package br.com.inseguros.ui.signup

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import br.com.inseguros.R
import br.com.inseguros.data.UserSession
import br.com.inseguros.databinding.SignUpFragmentBinding
import br.com.inseguros.ui.BaseFragment

class SignUpFragment : BaseFragment() {

    override val layout = R.layout.sign_up_fragment
    private var checkBoxDone = false
    private lateinit var mContext: Context
    private lateinit var binding: SignUpFragmentBinding
    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = SignUpFragmentBinding.inflate(layoutInflater)
        activity?.setContentView(binding.root)
        navController = Navigation.findNavController(view)

        setupListeners()

    }

    private fun setupListeners() {

        binding.useTermRegisterContainer.setOnClickListener {
            navController.navigate(R.id.action_signUpFragment_to_useTermFragment)
        }
        binding.useTermsAcceptLa.setOnClickListener {
            navController.navigate(R.id.action_signUpFragment_to_useTermFragment)
        }
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

}