package com.angellira.peregrino.model

data class Ocorrencias(
    val description: String,
    val value: Double,
    val isPositive: Boolean // true para positivo (verde), false para negativo (vermelho)
)
