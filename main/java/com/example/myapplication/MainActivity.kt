package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configurando os botões
        val buttonPvP = findViewById<Button>(R.id.button_pvp)
        val buttonCpu = findViewById<Button>(R.id.button_cpu)

        buttonPvP.setOnClickListener {
            // Iniciar a atividade PvP
            val intent = Intent(this, PvPActivity::class.java)
            startActivity(intent)
        }

        buttonCpu.setOnClickListener {
            // Iniciar a atividade Contra a Máquina
            val intent = Intent(this, CpuActivity::class.java)
            startActivity(intent)
        }
    }
}
