package br.com.inseguros.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import br.com.inseguros.utils.InSegurosTracker

abstract class BaseFragment : Fragment() {

    abstract val layout: Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val screenRootView = FrameLayout(requireContext())
        val screenView = inflater.inflate(layout, container, false)

        screenRootView.addView(screenView)

        return screenRootView
    }

    fun trackEvent(key: String, any: Any) {
        InSegurosTracker.trackEvent(requireActivity(), bundleOf(key to any))
    }

}