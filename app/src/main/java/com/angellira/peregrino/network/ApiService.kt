package com.angellira.peregrino.network

import com.angellira.peregrino.model.Corrida
import com.angellira.peregrino.model.HistoricoConsumo
import com.angellira.peregrino.model.Ocorrencias
import com.angellira.peregrino.model.Pneu
import com.angellira.peregrino.model.User
import com.angellira.peregrino.model.Veiculo
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
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
    @GET("Users.json")
    suspend fun getUsers(): Map<String, User>

    @POST("Users.json")
    suspend fun registrarUsuario(@Body user: User): Response<User>


    @GET("Users.json")
    suspend fun getUserByEmailAndPassword(
        @retrofit2.http.Query("cpf") cpf: String,
        @retrofit2.http.Query("senha") senha: String
    ): User

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: String): User

    @POST("Veiculos.json")
    suspend fun postVeiculos(@Body veiculo: Veiculo): Response<Veiculo>

    @GET("Veiculos.json")
    suspend fun getCars(): Map<String, Veiculo>

    @POST("Ocorrencias.json")
    suspend fun postOcorrencias(@Body ocorrencia: Ocorrencias): Response<Ocorrencias>

    @POST("Medias.json")
    suspend fun postHistoricoConsumo(@Body historicoConsumo: HistoricoConsumo): Response<HistoricoConsumo>

    @GET("Medias.json")
    suspend fun getMedias(): Map<String, HistoricoConsumo>

    @PATCH("Veiculos/{nodeId}.json")
    suspend fun updateVehicle(
        @Path("nodeId") nodeId: String, // ID do nó do Firebase
        @Body updatedFields: Map<String, String> // Campos a serem atualizados
    )

    @POST("Pneus.json")
    suspend fun postPneus(@Body pneu: Pneu): Response<Pneu>

    @GET("Pneus.json")
    suspend fun getPneus(): Map<String, Pneu>

    @POST("Corrida.json")
    suspend fun registrarCorrida(@Body corrida: Corrida)

    @GET("Corrida.json")
    suspend fun obterCorridas(): Map<String, Corrida>

    @GET("Ocorrencias.json")
    suspend fun getOcorrencias(): Map<String, Ocorrencias>

    @DELETE("Veiculos/{id}")
    suspend fun deleteVeiculo(@Path("id") id: String): Response<Unit>


}

object ApiServicePeregrino {
    val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
