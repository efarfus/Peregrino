package com.angellira.peregrino

import android.os.Bundle
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.angellira.peregrino.databinding.ActivityPneusBinding

class PneusActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPneusBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPneusBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.picCarro.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val x = event.x
                val y = event.y
                val width = v.width
                val height = v.height

                when {
                    x < width / 2 && y < height / 2 -> {
                        // Quadrante superior esquerdo (pneu dianteiro esquerdo)
                        handleTireClick("Dianteiro Esquerdo")
                    }
                    x >= width / 2 && y < height / 2 -> {
                        // Quadrante superior direito (pneu dianteiro direito)
                        handleTireClick("Dianteiro Direito")
                    }
                    x < width / 2 && y >= height / 2 -> {
                        // Quadrante inferior esquerdo (pneu traseiro esquerdo)
                        handleTireClick("Traseiro Esquerdo")
                    }
                    x >= width / 2 && y >= height / 2 -> {
                        // Quadrante inferior direito (pneu traseiro direito)
                        handleTireClick("Traseiro Direito")
                    }
                }

                // Chame performClick para fins de acessibilidade
                v.performClick()
            }
            true
        }
    }

    private fun handleTireClick(tirePosition: String) {
        // Exibe uma mensagem Toast com a posição do pneu clicado
        Toast.makeText(this, "Pneu $tirePosition clicado", Toast.LENGTH_SHORT).show()
    }
}
