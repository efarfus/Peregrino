package com.angellira.peregrino.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String = "",
    val name: String,
    val email: String,
    val senha: String,
    val cpf: String
)
