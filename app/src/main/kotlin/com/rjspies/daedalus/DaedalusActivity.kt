package com.rjspies.daedalus

import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import android.window.SplashScreen
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.core.animation.doOnEnd
import com.rjspies.daedalus.presentation.DaedalusTheme
import com.rjspies.daedalus.presentation.MainScreen

private const val ANIMATION_DURATION = 250L

class DaedalusActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Daedalus)
        enableEdgeToEdge()
        window?.isNavigationBarContrastEnforced = false

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            splashScreen.animateExit()
        }

        super.onCreate(savedInstanceState)

        setContent {
            DaedalusTheme {
                MainScreen()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun SplashScreen.animateExit() {
        setOnExitAnimationListener { view ->
            val slideDownAnimation = ObjectAnimator.ofFloat(view, View.ALPHA, 1f, 0f)
            slideDownAnimation.interpolator = LinearInterpolator()
            slideDownAnimation.duration = ANIMATION_DURATION
            slideDownAnimation.doOnEnd { view.remove() }
            slideDownAnimation.start()
        }
    }
}
