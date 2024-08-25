package com.example.myapplication

import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlin.concurrent.thread
import kotlin.random.Random

class PvPActivity : AppCompatActivity() {
    private lateinit var memoryGame: MemoryGame
    private var firstSelection: Int? = null
    private var player1Points = 0
    private var player2Points = 0
    private var currentPlayer = 1

    private lateinit var buttons: List<ImageButton>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pvp)

        memoryGame = MemoryGame(this)

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

        setupGrid()
    }

    private fun setupGrid() {
        buttons.forEach { button ->
            button.setImageResource(R.drawable.card_black)
            button.setOnClickListener {
                handleCardSelection(buttons.indexOf(button), button)
            }
        }
    }

    private fun handleCardSelection(index: Int, button: ImageButton) {
        if (firstSelection == null) {
            firstSelection = index
            button.setImageResource(memoryGame.getShuffledCards()[index])
        } else {
            if (firstSelection != index) {
                val firstButton = buttons[firstSelection!!]
                button.setImageResource(memoryGame.getShuffledCards()[index])

                if (memoryGame.getShuffledCards()[firstSelection!!] != memoryGame.getShuffledCards()[index]) {
                    // Se as cartas são diferentes, fechá-las
                    button.postDelayed({
                        firstButton.setImageResource(R.drawable.card_black)
                        button.setImageResource(R.drawable.card_black)
                        firstSelection = null
                        currentPlayer = if (currentPlayer == 1) 2 else 1 // Trocar de jogador
                    }, 1000)
                } else {
                    // Se as cartas são iguais
                    if (currentPlayer == 1) {
                        player1Points++
                    } else {
                        player2Points++
                    }
                    firstSelection = null
                }

                checkGameEnd()
            }
        }
    }

    private fun checkGameEnd() {
        if (memoryGame.isGameWon()) {
            val message = when {
                player1Points > player2Points -> getString(R.string.player1_wins)
                player2Points > player1Points -> getString(R.string.player2_wins)
                else -> getString(R.string.draw)
            }

            // Exibir o diálogo de resultado do jogo
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.game_over))
                .setMessage(message)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    finish() // Fecha a atividade após o jogo terminar
                }
                .show()
        }
    }
}
