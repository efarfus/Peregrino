package com.angellira.peregrino.model

import kotlinx.serialization.Serializable

@Serializable
data class Veiculo (
    var distancia: String = "",
    var enderecoAtual: String = "",
    var enderecoDestino: String = "",
    var gasto: String = "",
    var apelido: String = "",
    var modelo: String = "",
    var id: String = "",
    var lucro: String = "",
    var mediaVeiculo: String = "",
    var dataMediaFeita: String = "",
    var valorCorrida: String = ""
)