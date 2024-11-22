package com.angellira.peregrino.model

import kotlinx.serialization.Serializable

@Serializable
class Pneu {
    var ultimoEnchimento: String = ""
    var ultimaTroca: String = ""
    var fabricante: String = ""
    var posicao: String = ""
    var aro: String = ""
    var id: String = ""
    val carId: String = ""

}