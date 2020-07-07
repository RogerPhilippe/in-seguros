package br.com.inseguros.ui.quotes.motorcycle

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import br.com.inseguros.R
import br.com.inseguros.data.model.MainSubMenu
import br.com.inseguros.databinding.QuoteMotorcycleFragmentBinding
import br.com.inseguros.ui.BaseFragment

class QuoteMotorcycleFragment : BaseFragment() {

    private lateinit var binding: QuoteMotorcycleFragmentBinding
    override val layout = R.layout.quote_motorcycle_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = QuoteMotorcycleFragmentBinding.inflate(layoutInflater)
        activity?.setContentView(binding.root)

        val mainSubMenu = arguments?.get("main_sub_menu") as MainSubMenu
        binding.quotesMotorcycleToolbar.title = mainSubMenu.title

        binding.quotesMotorcycleToolbar.setNavigationOnClickListener {
            NavHostFragment.findNavController(this).popBackStack()
        }

    }

}