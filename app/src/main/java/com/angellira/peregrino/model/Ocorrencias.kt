package com.angellira.peregrino.model

data class Ocorrencias(
    val description: String,
    val value: Double,
    val isPositive: Boolean,
    val carId: String, // Identificador do carro
    val date: String // Data no formato "yyyy-MM-dd"
)

