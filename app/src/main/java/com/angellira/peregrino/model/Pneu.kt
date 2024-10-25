package com.angellira.peregrino.model

import kotlinx.serialization.Serializable

@Serializable
class Pneu {
    var kmAtual: String = ""
    var kmSubstituicao: String = ""
    var posicao: String = ""
    var status: String = ""
    var id: String = ""
}