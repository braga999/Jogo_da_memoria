package com.example.myapplication

import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.concurrent.thread
import kotlin.random.Random

class CpuActivity : AppCompatActivity() {
    private lateinit var memoryGame: MemoryGame
    private var firstSelection: Int? = null
    private var cpuTurn = false
    private var cpuSelections = mutableListOf<Int>()

    private lateinit var buttons: List<ImageButton>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cpu)

        memoryGame = MemoryGame(this)

        buttons = listOf(
            findViewById(R.id.button1),
            findViewById(R.id.button2),
            findViewById(R.id.button3),
            findViewById(R.id.button4),
            findViewById(R.id.button5),
            findViewById(R.id.button6),
            findViewById(R.id.button7),
            findViewById(R.id.button8),
            findViewById(R.id.button9),
            findViewById(R.id.button10),
            findViewById(R.id.button11),
            findViewById(R.id.button12),
            findViewById(R.id.button13),
            findViewById(R.id.button14),
            findViewById(R.id.button15),
            findViewById(R.id.button16)
        )

        setupGrid()
    }

    private fun setupGrid() {
        buttons.forEach { button ->
            button.setImageResource(R.drawable.card_black)
            button.setOnClickListener {
                if (!cpuTurn) {
                    handleCardSelection(buttons.indexOf(button), button)
                }
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

                // Se as cartas são diferentes
                if (memoryGame.getShuffledCards()[firstSelection!!] != memoryGame.getShuffledCards()[index]) {
                    // Fechar as cartas após um atraso
                    button.postDelayed({
                        firstButton.setImageResource(R.drawable.card_black)
                        button.setImageResource(R.drawable.card_black)
                        firstSelection = null
                        cpuTurn = true
                        cpuPlay()
                    }, 1000)
                } else {
                    // Se as cartas são iguais
                    memoryGame.selectCard(index) // Incrementa os pares do jogador
                    firstSelection = null
                    cpuTurn = true
                    cpuPlay()
                }

                checkGameEnd()
            }
        }
    }

    private fun cpuPlay() {
        thread {
            cpuSelections.clear()

            while (cpuSelections.size < 2) {
                val randomIndex = Random.nextInt(buttons.size)
                if (cpuSelections.contains(randomIndex) ||
                    buttons[randomIndex].drawable.constantState != resources.getDrawable(R.drawable.card_black).constantState) {
                    continue
                }

                cpuSelections.add(randomIndex)
                runOnUiThread {
                    buttons[randomIndex].setImageResource(memoryGame.getShuffledCards()[randomIndex])
                }

                Thread.sleep(1000)
            }

            runOnUiThread {
                // Se não houver correspondência
                if (memoryGame.getShuffledCards()[cpuSelections[0]] != memoryGame.getShuffledCards()[cpuSelections[1]]) {
                    buttons[cpuSelections[0]].postDelayed({
                        buttons[cpuSelections[0]].setImageResource(R.drawable.card_black)
                        buttons[cpuSelections[1]].setImageResource(R.drawable.card_black)
                    }, 1000)
                } else {
                    // Se houver correspondência, registrar os pares da CPU
                    memoryGame.selectCard(cpuSelections[0], true)
                }

                cpuTurn = false
                firstSelection = null

                checkGameEnd()
            }
        }
    }

    private fun checkGameEnd() {
        if (memoryGame.isGameWon()) {
            val message = when {
                memoryGame.matchedPairsPlayer > memoryGame.matchedPairsCpu -> getString(R.string.player_wins)
                memoryGame.matchedPairsPlayer < memoryGame.matchedPairsCpu -> getString(R.string.cpu_wins)
                else -> getString(R.string.draw)
            }

            // Exibir a mensagem do resultado do jogo
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            finish() // Fecha a atividade após o jogo terminar
        }
    }
}
