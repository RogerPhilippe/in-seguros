package br.com.inseguros.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import br.com.in_seguros_utils.makeShortToast
import br.com.inseguros.R
import br.com.inseguros.data.AppSession
import br.com.inseguros.data.UserSession
import br.com.inseguros.databinding.FragmentHomeBinding
import br.com.inseguros.ui.BaseFragment
import br.com.inseguros.ui.settings.SettingsActivity

class HomeFragment : BaseFragment() {

    override val layout = R.layout.fragment_home
    private lateinit var binding: FragmentHomeBinding
    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.inflate(layoutInflater)
        activity?.setContentView(binding.root)
        (activity as AppCompatActivity).setSupportActionBar(binding.homeToolbar)
        setHasOptionsMenu(true)
        navController = Navigation.findNavController(view)

        val items = AppSession.getMainMenuItems()
        val adapter = MainMenuAdapter(items, this)
        binding.mainMenuRV.adapter = adapter

        this.setupListeners()

    }

    override fun onResume() {
        super.onResume()
        trackEvent("home_fragment", "onResume")
        binding.userNameTV.text =
            if (UserSession.getUserName().isNotEmpty())
                UserSession.getUserName()
            else UserSession.getUserEmail()
    }

    private fun setupListeners() {

        // main_bottom_navigation
        binding.mainBottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.bottom_navigation_home -> {
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_notifications -> {
                    getString(R.string.not_implemented_in_alpha_yet).makeShortToast(requireContext())
                    return@setOnNavigationItemSelectedListener false
                }
                R.id.bottom_navigation_settings -> {
                    startActivity(Intent(requireContext(), SettingsActivity::class.java))
                    return@setOnNavigationItemSelectedListener false
                }
            }
            false
        }

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