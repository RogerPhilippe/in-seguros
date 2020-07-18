package br.com.inseguros.ui.useterm

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import br.com.inseguros.R
import br.com.inseguros.data.UserSession
import br.com.inseguros.databinding.UseTermFragmentBinding
import br.com.inseguros.ui.BaseFragment
import org.koin.android.viewmodel.ext.android.viewModel

class UseTermFragment : BaseFragment() {

    override val layout = R.layout.use_term_fragment
    private lateinit var navController: NavController
    private lateinit var binding: UseTermFragmentBinding
    private val mViewModel: UseTermViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = UseTermFragmentBinding.inflate(layoutInflater)
        activity?.setContentView(binding.root)
        navController = Navigation.findNavController(view)

        setupListeners()
        setupObservers()

        mViewModel.loadUseTermContent()

    }

    private fun setupListeners() {

        binding.useTermRejectBtn.setOnClickListener {
            UserSession.setUseTermSignUpState(false)
            navController.popBackStack()
        }
        binding.useTermAcceptBtn.setOnClickListener {
            UserSession.setUseTermSignUpState(true)
            navController.popBackStack()
        }

    }

    private fun setupObservers() {

        mViewModel.getUseTermContent().observe(viewLifecycleOwner, object : Observer<String> {
            override fun onChanged(useTermContent: String?) {
                binding.useTermContentTv.text = useTermContent
                mViewModel.getUseTermContent().removeObserver(this)
            }
        })
    }


}