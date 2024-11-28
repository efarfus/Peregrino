package com.angellira.peregrino.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    var id: String = "",
    var name: String = "",
    var email: String = "",
    var senha: String = "",
    var cpf: String = ""
)
