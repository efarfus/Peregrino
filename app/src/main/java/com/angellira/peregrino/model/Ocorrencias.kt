package com.angellira.peregrino.model

import kotlinx.serialization.Serializable

@Serializable
data class Ocorrencias(
    var description: String = "",
    var value: String = "",
    var isPositive: Boolean = true,
    var carId: String = "", // Identificador do carro
    var date: String  = ""// Data no formato "yyyy-MM-dd"
)

