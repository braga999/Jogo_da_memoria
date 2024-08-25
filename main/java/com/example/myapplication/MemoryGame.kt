package com.example.myapplication

import android.content.Context
import kotlin.random.Random

class MemoryGame(private val context: Context) {
    // Lista de imagens das cartas
    private val cardImages = listOf(
        R.drawable.card1, R.drawable.card2, R.drawable.card3,
        R.drawable.card4, R.drawable.card5, R.drawable.card6,
        R.drawable.card7, R.drawable.card8
    )
    
    private var shuffledCards = mutableListOf<Int>() // Cartas embaralhadas
    private var selectedCards = mutableListOf<Int>() // Cartas selecionadas
    var matchedPairsPlayer = 0 // Contador de pares do jogador
        private set
    var matchedPairsCpu = 0 // Contador de pares da CPU
        private set

    init {
        resetGame() // Inicializa o jogo
    }

    // Método para reiniciar o jogo
    fun resetGame() {
        shuffledCards.clear() // Limpa as cartas embaralhadas
        matchedPairsPlayer = 0 // Reseta o contador de pares do jogador
        matchedPairsCpu = 0 // Reseta o contador de pares da CPU
        selectedCards.clear() // Limpa as seleções de cartas
        val pairedCards = cardImages + cardImages // Duplicando as cartas para criar pares
        shuffledCards = pairedCards.shuffled(Random(System.currentTimeMillis())).toMutableList() // Embaralhando as cartas corretamente
    }

    // Método para selecionar uma carta
    fun selectCard(index: Int, isCpu: Boolean = false): Int? {
        selectedCards.add(index) // Adiciona o índice da carta selecionada

        // Verifica se duas cartas foram selecionadas
        if (selectedCards.size == 2) {
            val firstCard = shuffledCards[selectedCards[0]] // Primeira carta selecionada
            val secondCard = shuffledCards[selectedCards[1]] // Segunda carta selecionada

            // Se as cartas combinam
            if (firstCard == secondCard) {
                if (isCpu) {
                    matchedPairsCpu++ // Incrementa os pares da CPU
                } else {
                    matchedPairsPlayer++ // Incrementa os pares do jogador
                }
                selectedCards.clear() // Limpa as seleções de cartas
                return null // Retorna nulo se as cartas combinam
            } else {
                val firstIndex = selectedCards.removeAt(0) // Remove a primeira seleção
                return firstIndex // Retorna o índice da primeira carta selecionada para fechá-la
            }
        }
        return null // Retorna nulo se menos de duas cartas forem selecionadas
    }

    // Método para verificar se o jogo foi ganho
    fun isGameWon(): Boolean {
        return matchedPairsPlayer + matchedPairsCpu == cardImages.size // Verifica se todos os pares foram encontrados
    }

    // Método para obter as cartas embaralhadas
    fun getShuffledCards(): List<Int> {
        return shuffledCards // Retorna a lista de cartas embaralhadas
    }
}
