package br.com.inseguros.ui.messages

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import br.com.inseguros.R
import br.com.inseguros.databinding.MessagesFragmentBinding
import br.com.inseguros.ui.BaseFragment

class MessagesFragment : BaseFragment() {

    private lateinit var binding: MessagesFragmentBinding
    private val mViewModel: MessagesViewModel by viewModels()
    override val layout = R.layout.messages_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = MessagesFragmentBinding.inflate(layoutInflater)
        activity?.setContentView(binding.root)

        val toolBarTitle = arguments?.get("tool_bar_title") as String
        binding.messagesToolbar.title = toolBarTitle

        binding.messagesToolbar.setNavigationOnClickListener {
            NavHostFragment.findNavController(this).popBackStack()
        }

    }

    override fun onResume() {
        super.onResume()
        trackEvent("message_fragment", "onResume")
    }

}