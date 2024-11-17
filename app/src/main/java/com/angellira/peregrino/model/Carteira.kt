package com.angellira.peregrino.model

import kotlinx.serialization.Serializable

@Serializable
class Carteira {
    var data: String = ""
    var saldoLiquido: String = ""
    var saldoBruto: String = ""
}