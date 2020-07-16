package br.com.inseguros.ui.quotes.house

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import br.com.inseguros.R
import br.com.inseguros.data.model.MainSubMenu
import br.com.inseguros.databinding.QuoteHouseFragmentBinding
import br.com.inseguros.ui.BaseFragment

class QuoteHouseFragment : BaseFragment() {

    private lateinit var binding: QuoteHouseFragmentBinding
    override val layout = R.layout.quote_house_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = QuoteHouseFragmentBinding.inflate(layoutInflater)
        activity?.setContentView(binding.root)

        val mainSubMenu = arguments?.get("main_sub_menu") as MainSubMenu
        binding.quotesHouseToolbar.title = mainSubMenu.title

        binding.quotesHouseToolbar.setNavigationOnClickListener {
            NavHostFragment.findNavController(this).popBackStack()
        }

    }

    override fun onResume() {
        super.onResume()
        trackEvent("quote_house_fragment", "onResume")
    }

}