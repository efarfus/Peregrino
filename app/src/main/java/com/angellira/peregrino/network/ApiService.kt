package com.angellira.peregrino.network

import retrofit2.Call
import com.angellira.peregrino.model.Corrida
import com.angellira.peregrino.model.Ocorrencias
import com.angellira.peregrino.model.User
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


private const val BASE_URL = "https://peregrino-8435e-default-rtdb.firebaseio.com/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL).build()

interface ApiService {
    @GET("users.json")
    suspend fun getUsers(): List<User>

    @POST("users.json")
    suspend fun registrarUsuario(@Body user: User): User

    @GET("users/login.json")
    suspend fun getUserByEmailAndPassword(
        @retrofit2.http.Query("email") email: String,
        @retrofit2.http.Query("password") password: String
    ): User

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: String): User

    @POST("Corrida.json")
    suspend fun registrarCorrida(@Body corrida: Corrida)

    @GET("Corrida.json")
    suspend fun obterCorridas(): Map<String, Corrida>

    @GET("Ocorrencias.json")
    suspend fun getOcorrencias(): Map<String, Ocorrencias>

}

object ApiServicePeregrino {
    val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
