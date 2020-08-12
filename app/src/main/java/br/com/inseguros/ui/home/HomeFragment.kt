package br.com.inseguros.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Process
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.preference.PreferenceManager
import br.com.in_seguros_utils.makeShortToast
import br.com.inseguros.R
import br.com.inseguros.data.enums.QuoteTypeEnum
import br.com.inseguros.data.sessions.AppSession
import br.com.inseguros.data.sessions.UserSession
import br.com.inseguros.data.utils.Constants
import br.com.inseguros.databinding.FragmentHomeBinding
import br.com.inseguros.events.NotifyQuotationReceivedEvent
import br.com.inseguros.ui.BaseFragment
import br.com.inseguros.ui.settings.SettingsActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.android.viewmodel.ext.android.viewModel
import kotlin.system.exitProcess


class HomeFragment : BaseFragment() {

    override val layout = R.layout.fragment_home
    private lateinit var binding: FragmentHomeBinding
    private lateinit var navController: NavController
    private lateinit var mContext: Context
    private val mViewModel: HomeViewModel by viewModel()

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
        this.checkDefaultConfig()

        // Check malformed menus.
        if (AppSession.getMainMenuItems().size < 2 || AppSession.getMainSubMenuItems().size < 2) {
            "O App será reiniciado!".makeShortToast(requireContext())
            restartApp()
        }

    }

    override fun onResume() {
        super.onResume()
        trackEvent("home_fragment", "onResume")
        binding.userNameTV.text =
            if (UserSession.getUserName().isNotEmpty())
                UserSession.getUserName()
            else UserSession.getUserEmail()

        this.notifyQuotationReceived()
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    private fun checkDefaultConfig() {

        val prefs = PreferenceManager.getDefaultSharedPreferences(mContext)
        val containsAutoLogin = prefs.contains("auto_login")
        if (!containsAutoLogin)
            prefs.edit().putBoolean("auto_login", true).apply()

    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun notifyQuotationReceivedEvent(event: NotifyQuotationReceivedEvent) {
        this.notifyQuotationReceived()
    }

    private fun notifyQuotationReceived() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(mContext)
        val quoteIDStr = prefs.getString(Constants.NEW_QUOTATION_PROPOSAL_RECEIVED, "")
        val proposalID = prefs.getString(Constants.NEW_PROPOSAL_ID, "")
        if (!quoteIDStr.isNullOrEmpty() && !proposalID.isNullOrEmpty() )
            mViewModel.updateQuoteStatus(quoteIDStr, proposalID, QuoteTypeEnum.PROPOSAL_SENT.value)
    }

    private fun restartApp() {

        val intent = requireActivity().baseContext.packageManager
            .getLaunchIntentForPackage(requireActivity().baseContext.packageName)
        intent!!.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        Process.killProcess(Process.myPid())
        exitProcess(0)
    }

}