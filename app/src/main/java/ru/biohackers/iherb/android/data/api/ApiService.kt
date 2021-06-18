package ru.biohackers.iherb.android.data.api

import retrofit2.Response
import retrofit2.http.GET

data class GetResponse(
    val url: String,
)

interface ApiService {

    @GET("get")
    suspend fun get(): Response<GetResponse>

}
