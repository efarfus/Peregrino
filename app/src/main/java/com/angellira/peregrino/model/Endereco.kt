package com.angellira.peregrino.model

import kotlinx.serialization.Serializable

@Serializable
class Endereco {
    var id: String = ""
    var enderecoAtual: String = ""
    var enderecoDestino: String = ""
    var enderecoPassageiro: String = ""
    var horario: String = ""
    var coordenada: String = ""
}