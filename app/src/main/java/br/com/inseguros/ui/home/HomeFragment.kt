package br.com.inseguros.ui.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import br.com.inseguros.R
import br.com.inseguros.data.model.MainMenu
import br.com.inseguros.databinding.FragmentHomeBinding
import br.com.inseguros.ui.BaseFragment

class HomeFragment : BaseFragment() {

    override val layout = R.layout.fragment_home
    private val items = mutableListOf<MainMenu>()
    private lateinit var adapter: MainMenuAdapter
    private lateinit var binding: FragmentHomeBinding
    private lateinit var navController: NavController
    private val viewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.inflate(layoutInflater)
        activity?.setContentView(binding.root)
        (activity as AppCompatActivity).setSupportActionBar(binding.homeToolbar)
        setHasOptionsMenu(true)
        navController = Navigation.findNavController(view)

        adapter = MainMenuAdapter(items, this)
        binding.mainMenuRV.adapter = adapter

        viewModel.getMainMenuRemoteConfig()

        this.setupObservers()

    }

    private fun setupObservers() {

        viewModel.getCurrentMainMenuItem().observe(viewLifecycleOwner, object : Observer<List<MainMenu>>{
            override fun onChanged(mainMenuList: List<MainMenu>) {
                if (!mainMenuList.isNullOrEmpty()) {
                    items.clear()
                    items.addAll(mainMenuList)
                    adapter.notifyDataSetChanged()
                }
                viewModel.getCurrentMainMenuItem().removeObserver(this)
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_tool_bar_right_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId) {
            R.id.navigation_notifications -> { Toast.makeText(context, "Notifications", Toast.LENGTH_SHORT).show() }
            R.id.navigation_settings -> { Toast.makeText(context, "Settings", Toast.LENGTH_SHORT).show() }
        }

        return true
    }

    fun navControllerNavigateTo(id: Int, toolBarTitle: String) {
        val bundle = bundleOf("tool_bar_title" to toolBarTitle)
        navController.navigate(id, bundle)
    }

}