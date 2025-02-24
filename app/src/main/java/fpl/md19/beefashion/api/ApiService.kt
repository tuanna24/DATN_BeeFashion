package fpl.md19.beefashion.api

import fpl.md19.beefashion.models.UserModel
import fpl.md19.beefashion.requests.LoginRequest
import fpl.md19.beefashion.requests.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/auth/login")
    suspend fun Login(@Body request: LoginRequest): Response<UserModel>

    @POST("/auth/register")
    suspend fun Register(@Body request: RegisterRequest): Response<UserModel>
}