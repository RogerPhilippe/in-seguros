package br.com.inseguros.ui.quotes.life

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import br.com.inseguros.R
import br.com.inseguros.data.model.MainSubMenu
import br.com.inseguros.databinding.QuoteLifeFragmentBinding
import br.com.inseguros.ui.BaseFragment

class QuoteLifeFragment : BaseFragment() {

    private lateinit var binding: QuoteLifeFragmentBinding
    override val layout = R.layout.quote_life_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = QuoteLifeFragmentBinding.inflate(layoutInflater)
        activity?.setContentView(binding.root)

        val mainSubMenu = arguments?.get("main_sub_menu") as MainSubMenu
        binding.quotesLifeToolbar.title = mainSubMenu.title

        binding.quotesLifeToolbar.setNavigationOnClickListener {
            NavHostFragment.findNavController(this).popBackStack()
        }

    }

}