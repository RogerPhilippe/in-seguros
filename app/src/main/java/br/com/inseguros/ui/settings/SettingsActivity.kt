package br.com.inseguros.ui.settings

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import br.com.in_seguros_utils.makeShortToast
import br.com.inseguros.R
import br.com.inseguros.data.enums.SaveStatusEnum
import br.com.inseguros.databinding.SettingsActivityBinding
import br.com.inseguros.utils.DialogFragmentUtil
import org.koin.android.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: SettingsActivityBinding
    private val mViewModel: SettingsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SettingsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment(this))
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun setContentVisible(mainContentVisible: Boolean) {
        if (mainContentVisible) {
            binding.settings.visibility = View.VISIBLE
            binding.loadingContainer.visibility = View.GONE
        } else {
            binding.settings.visibility = View.GONE
            binding.loadingContainer.visibility = View.VISIBLE
        }
    }

    fun getViewModel() = mViewModel

    class SettingsFragment(
        private val parent: SettingsActivity
    ) : PreferenceFragmentCompat() {

        private lateinit var mViewModel: SettingsViewModel

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.settings_screen, rootKey)
            mViewModel = parent.getViewModel()
        }

        override fun onPreferenceTreeClick(preference: Preference?): Boolean {
            when(preference?.key) {
                "settings_sync" -> {
                    DialogFragmentUtil(
                        getString(R.string.cloud_sync_confirm_msg),
                        { syncAppWithFirebase() },
                        {}
                    ).show(requireActivity().supportFragmentManager, "sync_appWith_firebase")
                }
            }
            return true
        }

        private fun syncAppWithFirebase() {
            this.parent.setContentVisible(false)
            mViewModel.getQuotesFromFirebase()
            mViewModel.getCurrentSyncStatus().observe(viewLifecycleOwner, object : Observer<SaveStatusEnum> {
                override fun onChanged(status: SaveStatusEnum) {
                    if (status == SaveStatusEnum.SUCCESS) {
                        "Finalizado com sucesso.".makeShortToast(requireContext())
                    } else {
                        "Erro ao tentar sincronizar!".makeShortToast(requireContext())
                    }
                    parent.setContentVisible(true)
                    mViewModel.getCurrentSyncStatus().removeObserver(this)
                }
            })
        }


    }

}