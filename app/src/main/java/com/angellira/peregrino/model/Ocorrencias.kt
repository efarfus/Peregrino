package com.angellira.peregrino.model

import kotlinx.serialization.Serializable

@Serializable
data class Ocorrencias(
    var description: String = "",
    var value: String = "",
    var isPositive: Boolean = true,
    var carId: String = "",
    var date: String  = ""
)