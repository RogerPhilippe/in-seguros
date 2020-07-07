package br.com.inseguros.ui.historic

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import br.com.inseguros.R
import br.com.inseguros.databinding.HistoricFragmentBinding
import br.com.inseguros.ui.BaseFragment

class HistoricFragment : BaseFragment() {

    private lateinit var binding: HistoricFragmentBinding
    override val layout = R.layout.historic_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = HistoricFragmentBinding.inflate(layoutInflater)
        activity?.setContentView(binding.root)

        val toolBarTitle = arguments?.get("tool_bar_title") as String
        binding.historicToolbar.title = toolBarTitle

        binding.historicToolbar.setNavigationOnClickListener {
            NavHostFragment.findNavController(this).popBackStack()
        }

    }

}