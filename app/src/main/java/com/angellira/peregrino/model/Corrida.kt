package com.angellira.peregrino.model

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Corrida(
    val id: String,
    val custo: String,
    val pontoInicial: String,
    val pontoFinal: String,
    val ativa: Boolean = true,
    val data: String,
)