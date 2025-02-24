package fpl.md19.beefashion.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object HttpRequest {
    private const val URL = "http://beefashion.duckdns.org:9000"
    fun getInstance(): ApiService {
        return Retrofit
            .Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}