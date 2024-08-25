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
        enableEdgeToEdge() // Ativa o modo de tela cheia (edge-to-edge)
        setContentView(R.layout.activity_main) // Define o layout da atividade

        // Configura a aplicação dos insets da janela para evitar sobreposição de elementos
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            // Obtém os insets das barras do sistema (status e navegação)
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // Define o preenchimento da view principal com base nos insets
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets // Retorna os insets aplicados
        }

        // Configurando os botões
        val buttonPvP = findViewById<Button>(R.id.button_pvp) // Botão para o modo PvP
        val buttonCpu = findViewById<Button>(R.id.button_cpu) // Botão para o modo contra a CPU

        // Ação ao clicar no botão PvP
        buttonPvP.setOnClickListener {
            // Inicia a atividade PvP
            val intent = Intent(this, PvPActivity::class.java)
            startActivity(intent) // Inicia a nova atividade
        }

        // Ação ao clicar no botão Contra a Máquina
        buttonCpu.setOnClickListener {
            // Inicia a atividade Contra a Máquina
            val intent = Intent(this, CpuActivity::class.java)
            startActivity(intent) // Inicia a nova atividade
        }
    }
}
