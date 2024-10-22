package com.angellira.copiloto

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.angellira.copiloto.databinding.ActivityMainBinding
import com.angellira.copiloto.databinding.ActivitySplashBinding
import com.angellira.reservafrotas.preferences.Preferences
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.seconds

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val prefs by lazy { Preferences(this) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupView()
        checkLogin()

    }

    private fun setupView() {
        enableEdgeToEdge()
        binding = ActivitySplashBinding.inflate(layoutInflater)

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun checkLogin() {
        lifecycleScope.launch {
            delay(1.seconds)
            withContext(Main) {
                if (prefs.isLogged) {
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                } else {
                    startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                }
                finishAffinity()
            }
        }
    }
}