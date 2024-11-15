package com.angellira.peregrino.model

import kotlinx.serialization.Serializable

@Serializable
class CalculoViabilidade {
    var id: String = ""
    var lucro: String = ""
    var mediaVeiculo: String = ""
    var valorCorrida: String = ""
    var gasto: String = ""
    var enderecoDestino: String = ""
    var enderecoAtual: String = ""
    var distancia: String = ""
}