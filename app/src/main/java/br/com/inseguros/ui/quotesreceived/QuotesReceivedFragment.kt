package br.com.inseguros.ui.quotesreceived

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import br.com.inseguros.R
import br.com.inseguros.data.model.QuotationProposal
import br.com.inseguros.databinding.QuotesReceivedFragmentBinding
import br.com.inseguros.ui.BaseFragment
import org.koin.android.viewmodel.ext.android.viewModel

class QuotesReceivedFragment : BaseFragment() {

    override val layout = R.layout.quotes_received_fragment
    private lateinit var binding: QuotesReceivedFragmentBinding
    private val mViewModel: QuotesReceivedViewModel by viewModel()
    private var items = ArrayList<QuotationProposal>()
    private lateinit var mAdapter: QuoteReceivedAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = QuotesReceivedFragmentBinding.inflate(layoutInflater)
        activity?.setContentView(binding.root)

        val toolBarTitle = arguments?.get("tool_bar_title") as String
        binding.quotesReceivedToolbar.title = toolBarTitle

        // quotes_received_rv
        mAdapter = QuoteReceivedAdapter(items)
        binding.quotesReceivedRv.adapter = mAdapter

        mViewModel.getQuotations()

        setupObservables()
        setupListeners()

    }

    private fun setupObservables() {

        mViewModel.getCurrentQuotationListLiveData().observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                items.clear()
                items.addAll(it)
                mAdapter.notifyDataSetChanged()
            }
        })
    }

    private fun setupListeners() {

        binding.quotesReceivedToolbar.setNavigationOnClickListener {
            NavHostFragment.findNavController(this).popBackStack()
        }
    }

    override fun onResume() {
        super.onResume()
        trackEvent("quote_received_fragment", "onResume")
    }

}