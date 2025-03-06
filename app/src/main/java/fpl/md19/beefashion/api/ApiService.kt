package fpl.md19.beefashion.api

import fpl.md19.beefashion.models.AddressModel
import fpl.md19.beefashion.models.Brand
import fpl.md19.beefashion.models.Categories
import fpl.md19.beefashion.models.ProductDetails
import fpl.md19.beefashion.models.Products
import fpl.md19.beefashion.models.Sizes
import fpl.md19.beefashion.models.UserModel
import fpl.md19.beefashion.requests.AddressRequest
import fpl.md19.beefashion.requests.LoginRequest
import fpl.md19.beefashion.requests.RegisterRequest
import fpl.md19.beefashion.viewModels.District
import fpl.md19.beefashion.viewModels.Province
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Part


interface ApiService {
    @POST("/auth/login")
    suspend fun Login(@Body request: LoginRequest): Response<UserModel>

    @POST("/auth/register")
    suspend fun Register(@Body request: RegisterRequest): Response<UserModel>


    @GET("/addresses/{customerId}")
    suspend fun getAllAddresses(@Path("customerId") customerId: String): Response<List<AddressModel>>

    @POST("/addresses/{customerId}")
    suspend fun createAddress(
        @Path("customerId") customerId: String,
        @Body addressRequest: AddressRequest
    ): Response<AddressModel>

    @DELETE("/addresses/{customerId}/{id}")
    suspend fun deleteAddress(
        @Path("customerId") customerId: String,
        @Path("id") addressId: String
    ): Response<Unit>

    @PUT("/addresses/{customerId}/{id}")
    suspend fun updateAddress(
        @Path("customerId") customerId: String,
        @Path("id") addressId: String,
        @Body addressRequest: AddressRequest
    ): Response<AddressModel>

    @GET("https://provinces.open-api.vn/api/p/")
    suspend fun getProvinces(): Response<List<Province>>

    @GET("https://provinces.open-api.vn/api/p/{province_code}?depth=2")
    suspend fun getDistricts(@Path("province_code") provinceCode: String): Response<Province>

    @GET("https://provinces.open-api.vn/api/d/{district_code}?depth=2")
    suspend fun getWards(@Path("district_code") districtCode: String): Response<District>

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