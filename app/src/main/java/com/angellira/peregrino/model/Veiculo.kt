package com.angellira.peregrino.model

import kotlinx.serialization.Serializable

@Serializable
class Veiculo {
    var distancia: String = ""
    var enderecoAtual: String = ""
    var enderecoDestino: String = ""
    var gasto: String = ""
    var id: String = ""
    var lucro: String = ""
    var mediaVeiculo: String = ""
    var valorCorrida: String = ""
}