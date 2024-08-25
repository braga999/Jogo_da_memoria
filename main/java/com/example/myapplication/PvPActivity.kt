package com.example.myapplication

import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlin.concurrent.thread
import kotlin.random.Random

class PvPActivity : AppCompatActivity() {
    private lateinit var memoryGame: MemoryGame // Instância do jogo da memória
    private var firstSelection: Int? = null // Armazena o índice da primeira carta selecionada
    private var player1Points = 0 // Contador de pontos do jogador 1
    private var player2Points = 0 // Contador de pontos do jogador 2
    private var currentPlayer = 1 // Armazena qual jogador está na vez

    private lateinit var buttons: List<ImageButton> // Lista de botões que representam as cartas

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pvp) // Define o layout da atividade

        memoryGame = MemoryGame(this) // Inicializa o jogo da memória

        buttons = listOf(
            findViewById(R.id.pvp_button1),
            findViewById(R.id.pvp_button2),
            findViewById(R.id.pvp_button3),
            findViewById(R.id.pvp_button4),
            findViewById(R.id.pvp_button5),
            findViewById(R.id.pvp_button6),
            findViewById(R.id.pvp_button7),
            findViewById(R.id.pvp_button8),
            findViewById(R.id.pvp_button9),
            findViewById(R.id.pvp_button10),
            findViewById(R.id.pvp_button11),
            findViewById(R.id.pvp_button12),
            findViewById(R.id.pvp_button13),
            findViewById(R.id.pvp_button14),
            findViewById(R.id.pvp_button15),
            findViewById(R.id.pvp_button16)
        )

        setupGrid() // Configura a grade de botões
    }

    // Configura os botões e define suas ações
    private fun setupGrid() {
        buttons.forEach { button ->
            button.setImageResource(R.drawable.card_black) // Define a imagem padrão das cartas
            button.setOnClickListener {
                handleCardSelection(buttons.indexOf(button), button) // Trata a seleção da carta
            }
        }
    }

    // Trata a seleção de uma carta
    private fun handleCardSelection(index: Int, button: ImageButton) {
        if (firstSelection == null) {
            firstSelection = index // Armazena o índice da primeira seleção
            button.setImageResource(memoryGame.getShuffledCards()[index]) // Mostra a carta selecionada
        } else {
            if (firstSelection != index) { // Garante que não se pode selecionar a mesma carta
                val firstButton = buttons[firstSelection!!] // Referência para o botão da primeira seleção
                button.setImageResource(memoryGame.getShuffledCards()[index]) // Mostra a segunda carta selecionada

                // Verifica se as cartas são diferentes
                if (memoryGame.getShuffledCards()[firstSelection!!] != memoryGame.getShuffledCards()[index]) {
                    // Se as cartas são diferentes, fechá-las após um atraso
                    button.postDelayed({
                        firstButton.setImageResource(R.drawable.card_black) // Fecha a primeira carta
                        button.setImageResource(R.drawable.card_black) // Fecha a segunda carta
                        firstSelection = null // Limpa a seleção
                        currentPlayer = if (currentPlayer == 1) 2 else 1 // Troca de jogador
                    }, 1000)
                } else {
                    // Se as cartas são iguais, incrementa os pontos do jogador atual
                    if (currentPlayer == 1) {
                        player1Points++
                    } else {
                        player2Points++
                    }
                    firstSelection = null // Limpa a seleção
                }

                checkGameEnd() // Verifica se o jogo terminou
            }
        }
    }

    // Verifica se o jogo terminou
    private fun checkGameEnd() {
        if (memoryGame.isGameWon()) { // Se o jogo foi ganho
            val message = when {
                player1Points > player2Points -> getString(R.string.player1_wins) // Jogador 1 vence
                player2Points > player1Points -> getString(R.string.player2_wins) // Jogador 2 vence
                else -> getString(R.string.draw) // Empate
            }

            // Exibir o diálogo de resultado do jogo
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.game_over)) // Título do diálogo
                .setMessage(message) // Mensagem com o resultado
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    finish() // Fecha a atividade após o jogo terminar
                }
                .show() // Mostra o diálogo
        }
    }
}
