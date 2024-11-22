package com.angellira.peregrino.model

import java.util.UUID

data class Car(
    val nickname: String, // Apelido do carro
    val model: String, // Modelo do carro
    val id: String = UUID.randomUUID().toString()
)
