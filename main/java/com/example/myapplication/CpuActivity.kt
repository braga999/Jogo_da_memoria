package com.example.myapplication

import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.concurrent.thread
import kotlin.random.Random

class CpuActivity : AppCompatActivity() {
    // Variáveis para o jogo da memória
    private lateinit var memoryGame: MemoryGame
    private var firstSelection: Int? = null // Índice da primeira carta selecionada
    private var cpuTurn = false // Indica se é a vez da CPU
    private var cpuSelections = mutableListOf<Int>() // Seleções da CPU

    private lateinit var buttons: List<ImageButton> // Lista de botões (cartas)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cpu)

        // Inicializa o jogo da memória
        memoryGame = MemoryGame(this)

        // Inicializa a lista de botões (cartas)
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

        setupGrid() // Configura a grade de cartas
    }

    private fun setupGrid() {
        // Inicializa cada botão com a carta virada para baixo
        buttons.forEach { button ->
            button.setImageResource(R.drawable.card_black) // Imagem da carta virada para baixo
            button.setOnClickListener {
                if (!cpuTurn) { // Verifica se não é a vez da CPU
                    handleCardSelection(buttons.indexOf(button), button) // Lida com a seleção da carta
                }
            }
        }
    }

    private fun handleCardSelection(index: Int, button: ImageButton) {
        if (firstSelection == null) { // Se nenhuma carta foi selecionada ainda
            firstSelection = index // Armazena o índice da primeira seleção
            button.setImageResource(memoryGame.getShuffledCards()[index]) // Revela a carta
        } else {
            if (firstSelection != index) { // Se a segunda carta selecionada é diferente da primeira
                val firstButton = buttons[firstSelection!!] // Obtém o botão da primeira seleção
                button.setImageResource(memoryGame.getShuffledCards()[index]) // Revela a segunda carta

                // Se as cartas são diferentes
                if (memoryGame.getShuffledCards()[firstSelection!!] != memoryGame.getShuffledCards()[index]) {
                    // Fecha as cartas após um atraso
                    button.postDelayed({
                        firstButton.setImageResource(R.drawable.card_black) // Vira a primeira carta de volta
                        button.setImageResource(R.drawable.card_black) // Vira a segunda carta de volta
                        firstSelection = null // Reseta a seleção
                        cpuTurn = true // Passa a vez para a CPU
                        cpuPlay() // Chama a função para a jogada da CPU
                    }, 1000) // Atraso de 1 segundo
                } else {
                    // Se as cartas são iguais
                    memoryGame.selectCard(index) // Incrementa os pares do jogador
                    firstSelection = null // Reseta a seleção
                    cpuTurn = true // Passa a vez para a CPU
                    cpuPlay() // Chama a função para a jogada da CPU
                }

                checkGameEnd() // Verifica se o jogo acabou
            }
        }
    }

    private fun cpuPlay() {
        thread {
            cpuSelections.clear() // Limpa as seleções da CPU

            // A CPU joga até selecionar 2 cartas
            while (cpuSelections.size < 2) {
                val randomIndex = Random.nextInt(buttons.size) // Seleciona um índice aleatório
                // Verifica se a carta já foi revelada ou selecionada
                if (cpuSelections.contains(randomIndex) ||
                    buttons[randomIndex].drawable.constantState != resources.getDrawable(R.drawable.card_black).constantState) {
                    continue // Continua se a carta já foi revelada
                }

                cpuSelections.add(randomIndex) // Adiciona o índice à lista de seleções da CPU
                runOnUiThread {
                    buttons[randomIndex].setImageResource(memoryGame.getShuffledCards()[randomIndex]) // Revela a carta
                }

                Thread.sleep(1000) // Atraso para simular a seleção da carta
            }

            runOnUiThread {
                // Se as cartas não correspondem
                if (memoryGame.getShuffledCards()[cpuSelections[0]] != memoryGame.getShuffledCards()[cpuSelections[1]]) {
                    buttons[cpuSelections[0]].postDelayed({
                        buttons[cpuSelections[0]].setImageResource(R.drawable.card_black) // Vira a primeira carta de volta
                        buttons[cpuSelections[1]].setImageResource(R.drawable.card_black) // Vira a segunda carta de volta
                    }, 1000) // Atraso de 1 segundo
                } else {
                    // Se as cartas correspondem, registrar os pares da CPU
                    memoryGame.selectCard(cpuSelections[0], true)
                }

                cpuTurn = false // Passa a vez para o jogador
                firstSelection = null // Reseta a seleção

                checkGameEnd() // Verifica se o jogo acabou
            }
        }
    }

    private fun checkGameEnd() {
        // Verifica se o jogo foi ganho
        if (memoryGame.isGameWon()) {
            // Define a mensagem com base no resultado
            val message = when {
                memoryGame.matchedPairsPlayer > memoryGame.matchedPairsCpu -> getString(R.string.player_wins) // Jogador ganha
                memoryGame.matchedPairsPlayer < memoryGame.matchedPairsCpu -> getString(R.string.cpu_wins) // CPU ganha
                else -> getString(R.string.draw) // Empate
            }

            // Exibe a mensagem do resultado do jogo
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            finish() // Fecha a atividade após o jogo terminar
        }
    }
}
