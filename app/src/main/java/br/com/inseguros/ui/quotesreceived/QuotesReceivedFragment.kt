package br.com.inseguros.ui.quotesreceived

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import br.com.inseguros.R
import br.com.inseguros.databinding.QuotesReceivedFragmentBinding
import br.com.inseguros.ui.BaseFragment

class QuotesReceivedFragment : BaseFragment() {

    private lateinit var binding: QuotesReceivedFragmentBinding
    private val mViewModel: QuotesReceivedViewModel by viewModels()
    override val layout = R.layout.quotes_received_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = QuotesReceivedFragmentBinding.inflate(layoutInflater)
        activity?.setContentView(binding.root)

        val toolBarTitle = arguments?.get("tool_bar_title") as String
        binding.quotesReceivedToolbar.title = toolBarTitle

        binding.quotesReceivedToolbar.setNavigationOnClickListener {
            NavHostFragment.findNavController(this).popBackStack()
        }

    }

}