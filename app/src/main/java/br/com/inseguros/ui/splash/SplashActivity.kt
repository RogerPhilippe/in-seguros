package br.com.inseguros.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import br.com.inseguros.R
import br.com.inseguros.data.sessions.AppSession
import br.com.inseguros.ui.MainActivity

class SplashActivity : AppCompatActivity() {

    private var mMainMenuLoaded = false
    private var mMainSubMenuLoaded = false
    private var mMinimumSplashTimeFinished = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        setupObservers()
        setupSplashScreenMinimumTime()

        AppSession.setMainMenuItems()

    }

    private fun setupObservers() {

        AppSession.getMainMenuStatus().observe(this, object : Observer<Boolean> {
            override fun onChanged(t: Boolean) {
                mMainMenuLoaded = t
                if (mMainSubMenuLoaded && mMinimumSplashTimeFinished)
                    loadMainActivity()
                AppSession.getMainMenuStatus().removeObserver(this)
            }
        })

        AppSession.getMainSubMenuStatus().observe(this, object : Observer<Boolean> {
            override fun onChanged(t: Boolean) {
                mMainSubMenuLoaded = t
                if (mMainMenuLoaded && mMinimumSplashTimeFinished)
                    loadMainActivity()
                AppSession.getMainSubMenuStatus().removeObserver(this)
            }
        })
    }

    private fun setupSplashScreenMinimumTime() {

        object : CountDownTimer(2000, 1000) {

            override fun onTick(p0: Long) {}

            override fun onFinish() {
                mMinimumSplashTimeFinished = true
                if (mMainMenuLoaded && mMainSubMenuLoaded)
                    loadMainActivity()
            }
        }.start()
    }

    private fun loadMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        this.finish()
    }
}