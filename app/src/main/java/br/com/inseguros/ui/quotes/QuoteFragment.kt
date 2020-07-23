package br.com.inseguros.ui.quotes

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import br.com.inseguros.R
import br.com.inseguros.data.sessions.AppSession
import br.com.inseguros.data.model.MainSubMenu
import br.com.inseguros.databinding.QuoteFragmentBinding
import br.com.inseguros.ui.BaseFragment

class QuoteFragment : BaseFragment() {

    private lateinit var binding: QuoteFragmentBinding
    private lateinit var navController: NavController
    override val layout = R.layout.quote_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = QuoteFragmentBinding.inflate(layoutInflater)
        activity?.setContentView(binding.root)
        navController = Navigation.findNavController(view)

        val toolBarTitle = arguments?.get("tool_bar_title") as String
        binding.quoteToolbar.title = toolBarTitle

        val items = AppSession.getMainSubMenuItems()
        val adapter = QuoteAdapter(items, this)
        binding.qutesRV.adapter = adapter

        binding.quoteToolbar.setNavigationOnClickListener {
            NavHostFragment.findNavController(this).popBackStack()
        }

    }

    override fun onResume() {
        super.onResume()
        trackEvent("quote_fragment", "onResume")
    }

    fun navControllerNavigateTo(id: Int, mainSubMenu: MainSubMenu) {
        val bundle = bundleOf("main_sub_menu" to mainSubMenu)
        navController.navigate(id, bundle)
    }

}