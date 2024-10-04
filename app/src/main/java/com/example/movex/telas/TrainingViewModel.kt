package com.example.movex.telas

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel

class TrainingViewModel : ViewModel() {
    private val _progressMap = mutableStateMapOf<String, Float>()

    // Usa o derivedStateOf para criar uma versão imutável do mapa de progresso
    val progressMap: State<Map<String, Float>> = derivedStateOf { _progressMap.toMap() }

    fun incrementProgress(day: String, increment: Float = 10f) {
        // Incrementa o progresso em um valor especificado
        val currentProgress = _progressMap[day] ?: 0f
        val newProgress = (currentProgress + increment).coerceAtMost(100f) // Limita a 100%
        _progressMap[day] = newProgress
        // Log para debug
        println("Progresso de $day atualizado: $newProgress%")
    }

    fun getProgress(day: String): Float {
        return _progressMap[day] ?: 0f
    }
}