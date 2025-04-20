package fpl.md19.beefashion.api

import fpl.md19.beefashion.models.AddressModel
import fpl.md19.beefashion.models.Brand
import fpl.md19.beefashion.models.Carts
import fpl.md19.beefashion.models.CartItem
import fpl.md19.beefashion.models.CartItemSentData
import fpl.md19.beefashion.models.Categories
import fpl.md19.beefashion.models.MyOder
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
import retrofit2.http.PATCH
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

    @GET("/products/{customerID}")
    suspend fun getProductsWithCustomerID(@Path("customerID") customerID: String): Response<List<Products>>

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
        @Part image: MultipartBody.Part?
    ): Response<UserModel>

    @GET("/favorites/{customerID}")
    suspend fun getFavoriteProducts(@Path("customerID") customerID: String): Response<List<Products>>

    @POST("/favorites/{customerID}/{productID}")
    suspend fun addFavoriteProduct(@Path("customerID") customerID: String, @Path("productID") productID: String): Response<Unit>

    @DELETE("/favorites/{customerID}/{productID}")
    suspend fun removeFavoriteProduct(@Path("customerID") customerID: String, @Path("productID") productID: String): Response<Unit>

    @GET("/carts/{customerID}")
    suspend fun getCartProducts(@Path("customerID") customerID: String): Response<List<CartItem>>

    @POST("/carts/{customerID}/{productID}")
    suspend fun addProductToCart(@Path("customerID") customerID: String, @Path("productID") productID: String, @Body newCartItem: CartItemSentData): Response<Unit>

    @PUT("/carts/{customerID}/{productID}")
    suspend fun changeProductQuantityInCart(@Path("customerID") customerID: String, @Path("productID") productID: String, @Body cartItem: CartItemSentData): Response<CartItem>

    @DELETE("/carts/{customerID}/{productID}/{sizeID}")
    suspend fun removeProductFromCart(@Path("customerID") customerID: String, @Path("productID") productID: String, @Path("sizeID") sizeID: String): Response<Unit>

    @GET("/invoices/{customerID}")
    suspend fun getInvoices(@Path("customerID") customerID: String): Response<List<MyOder>>

    @POST("/invoices")
    suspend fun makeAnInvoice(@Body invoiceDTO: MyOder): Response<Unit>

    @DELETE("/invoices/{customerID}/{invoiceID}")
    suspend fun cancelInvoice(@Path("customerID") customerID: String, @Path("invoiceID") invoiceID: String): Response<Unit>

    @PATCH("/invoices/{customerID}/{invoiceID}")
    suspend fun completeInvoice(@Path("customerID") customerID: String, @Path("invoiceID") invoiceID: String): Response<Unit>
}