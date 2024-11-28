package com.angellira.peregrino.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class User(
    var id: String = "",

    @SerialName("CPF") // Certifique-se de usar a anotação @SerialName
    var CPF: String = "",

    @SerialName("Nome") // Usando a anotação para garantir que 'Nome' é reconhecido corretamente
    var nome: String = "",

    @SerialName("Email") // Para garantir que 'Email' seja reconhecido corretamente
    var email: String = "",

    @SerialName("Senha") // Para 'Senha'
    var senha: String = ""
)
