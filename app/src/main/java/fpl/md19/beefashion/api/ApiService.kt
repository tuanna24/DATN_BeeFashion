package fpl.md19.beefashion.api

import fpl.md19.beefashion.models.Brand
import fpl.md19.beefashion.models.Categories
import fpl.md19.beefashion.models.ProductDetails
import fpl.md19.beefashion.models.Products
import fpl.md19.beefashion.models.Sizes
import fpl.md19.beefashion.models.UserModel
import fpl.md19.beefashion.requests.LoginRequest
import fpl.md19.beefashion.requests.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
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
}