package com.angellira.peregrino.model

import kotlinx.serialization.Serializable

@Serializable
class User {
    var id: String = ""
    var cpf: String = ""
    var nome: String = ""
    var email: String = ""
    var senha: String = ""
}