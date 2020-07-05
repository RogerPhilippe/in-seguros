package br.com.inseguros.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment

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

}