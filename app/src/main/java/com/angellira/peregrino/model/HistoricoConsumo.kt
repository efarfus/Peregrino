package com.angellira.peregrino.model

import kotlinx.serialization.Serializable

@Serializable
data class HistoricoConsumo(
    var id: String = "",
    var data: String = "",
    var eficiencia: String = ""
)
