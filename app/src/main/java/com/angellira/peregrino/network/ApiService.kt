package com.angellira.peregrino.network

import com.angellira.peregrino.model.User
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path


private const val BASE_URL = "http://192.168.115.151:8080/"


private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
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

}

object ApiServicePeregrino {
    val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
