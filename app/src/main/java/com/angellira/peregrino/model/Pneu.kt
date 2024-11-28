package com.angellira.peregrino.model

import kotlinx.serialization.Serializable

@Serializable
data class Pneu(
    var id: String = "",
    var carId: String = "",
    var posicao: String = "",
    var fabricante: String = "",
    var aro: String = "",
    var ultimoEnchimento: String = "",
    var ultimaTroca: String = "",
    val timestamp: Long = System.currentTimeMillis() // Timestamp padrão
)
