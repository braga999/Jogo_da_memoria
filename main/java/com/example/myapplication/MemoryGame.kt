package com.example.myapplication

import android.content.Context
import kotlin.random.Random

class MemoryGame(private val context: Context) {
    private val cardImages = listOf(
        R.drawable.card1, R.drawable.card2, R.drawable.card3,
        R.drawable.card4, R.drawable.card5, R.drawable.card6,
        R.drawable.card7, R.drawable.card8
    )
    private var shuffledCards = mutableListOf<Int>()
    private var selectedCards = mutableListOf<Int>()
    var matchedPairsPlayer = 0 // Contador de pares do jogador
        private set
    var matchedPairsCpu = 0 // Contador de pares da CPU
        private set

    init {
        resetGame()
    }

    fun resetGame() {
        shuffledCards.clear()
        matchedPairsPlayer = 0
        matchedPairsCpu = 0
        selectedCards.clear()
        val pairedCards = cardImages + cardImages // Duplicando as cartas para criar pares
        shuffledCards = pairedCards.shuffled(Random(System.currentTimeMillis())).toMutableList() // Embaralhando as cartas corretamente
    }

    fun selectCard(index: Int, isCpu: Boolean = false): Int? {
        selectedCards.add(index)

        if (selectedCards.size == 2) {
            val firstCard = shuffledCards[selectedCards[0]]
            val secondCard = shuffledCards[selectedCards[1]]

            if (firstCard == secondCard) {
                if (isCpu) {
                    matchedPairsCpu++ // Incrementa os pares da CPU
                } else {
                    matchedPairsPlayer++ // Incrementa os pares do jogador
                }
                selectedCards.clear()
                return null // Retornar nulo se as cartas combinam
            } else {
                val firstIndex = selectedCards.removeAt(0) // Remove a primeira seleção
                return firstIndex // Retorna o índice da primeira carta selecionada para fechá-la
            }
        }
        return null
    }

    fun isGameWon(): Boolean {
        return matchedPairsPlayer + matchedPairsCpu == cardImages.size // Verifica se todos os pares foram encontrados
    }

    fun getShuffledCards(): List<Int> {
        return shuffledCards
    }
}
