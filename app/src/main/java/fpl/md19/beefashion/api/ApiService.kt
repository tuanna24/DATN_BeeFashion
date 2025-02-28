package fpl.md19.beefashion.api

import fpl.md19.beefashion.models.Brand
import fpl.md19.beefashion.models.Categories
import fpl.md19.beefashion.models.ProductDetails
import fpl.md19.beefashion.models.Products
import fpl.md19.beefashion.models.Sizes
import fpl.md19.beefashion.models.UserModel
import fpl.md19.beefashion.requests.LoginRequest
import fpl.md19.beefashion.requests.RegisterRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @POST("/auth/login")
    suspend fun Login(@Body request: LoginRequest): Response<UserModel>

    @POST("/auth/register")
    suspend fun Register(@Body request: RegisterRequest): Response<UserModel>

    @GET("/products")
    suspend fun getProducts(): Response<List<Products>>

    @GET("/categories")
    suspend fun getCategories() : Response<List<Categories>>

    @GET("/productdetails/getByProductID/{productId}")
    suspend fun getProductDetails(@Path("productId") productId: String): ProductDetails

    @GET("/brands")
    suspend fun getBrands() : Response<List<Brand>>

    @GET("/sizes")
    suspend fun getSizes() : Response<List<Sizes>>

    @Multipart
    @PUT("/customers/{id}")
    suspend fun EditProfile(
        @Path("id") id: String,
        @Part("email") email: RequestBody,
        @Part("fullName") fullName: RequestBody,
        @Part("phone") phone: RequestBody?,
        @Part("gender") gender: RequestBody?,
        @Part("dateOfBirth") dateOfBirth: RequestBody?,
        @Part file: MultipartBody.Part?
    ): Response<UserModel>
}