package com.angellira.peregrino.model

import kotlinx.serialization.Serializable

@Serializable
class Corrida {
    var id: String = ""
    var horario: String = ""
    var enderecoAtual: String = ""
    var enderecoPassageiro: String = ""
    var enderecoDestino: String = ""
}