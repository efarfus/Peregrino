package com.angellira.peregrino.network

import retrofit2.Call
import com.angellira.peregrino.model.Corrida
import com.angellira.peregrino.model.User
import com.angellira.peregrino.model.Veiculo
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


private const val BASE_URL = "https://peregrino-8435e-default-rtdb.firebaseio.com/"

val json = Json {
    ignoreUnknownKeys = true // Isso irá ignorar qualquer chave desconhecida
}

private val retrofit = Retrofit.Builder()

    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL).build()

interface ApiService {
    @GET("users.json")
    suspend fun getUsers(): List<User>

    @GET("/users/login.json")
    suspend fun getUserByEmailAndPassword(
        @retrofit2.http.Query("email") email: String,
        @retrofit2.http.Query("password") password: String
    ): User

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: String): User

    @POST("Veiculos.json")
    suspend fun postVeiculos(@Body veiculo: Veiculo): Response<Veiculo>

    @GET("Veiculos.json")
    suspend fun getCars(): Map<String, Veiculo>
}

object ApiServicePeregrino {
    val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
