package fpl.md19.beefashion.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fpl.md19.beefashion.GlobalVarible.UserSesion
import fpl.md19.beefashion.api.ApiService
import fpl.md19.beefashion.api.HttpRequest
import fpl.md19.beefashion.models.CartItem
import fpl.md19.beefashion.models.CartItemSentData
import fpl.md19.beefashion.models.Products
import kotlinx.coroutines.launch

class CartViewModel: ViewModel() {
    private val apiService: ApiService = HttpRequest.getInstance()

    private var _cartItems = MutableLiveData<MutableList<CartItem>>()
    var cartItems: LiveData<MutableList<CartItem>> = _cartItems

    private var _cartItem = MutableLiveData<CartItem?>()
    var cartItem: LiveData<CartItem?> = _cartItem

    fun getCartItems(){
        viewModelScope.launch {
            try{
                val customerID = UserSesion.currentUser?.id
                if(!customerID.isNullOrBlank()){
                    val favProductsRes = apiService.getCartProducts(customerID)
                    if(favProductsRes.code() == 200){
                        _cartItems.postValue(favProductsRes.body()?.toMutableList())
                    }else{
                        _cartItems.postValue(mutableListOf())
                    }
                }
            }catch (e: Exception){
                println(e)
            }
        }
    }

    fun updateCartItems(cartItem: CartItemSentData){
        viewModelScope.launch {
            try{
                val customerID = UserSesion.currentUser?.id
                if(!customerID.isNullOrBlank()){
                    val response = apiService.changeProductQuantityInCart(customerID, cartItem.productId, cartItem)
                    if(response.isSuccessful && response.code() == 200){
                        _cartItem.postValue(response.body())
                        getCartItems()
//                        _cartItems.value?.replaceAll{
//                            if ( it.productId == cartItem.productId && it.sizeID == cartItem.sizeID ) response.body()!! else it
//                        }
                    }else{
                        _cartItem.postValue(null)
                    }
                }
            }catch (e: Exception){
                println(e)
            }
        }
    }

    fun addProductToCart(cartItem: CartItemSentData){
        viewModelScope.launch {
            try{
                val customerID = UserSesion.currentUser?.id
                if(!customerID.isNullOrBlank()){
                    val response = apiService.addProductToCart(customerID, cartItem.productId, cartItem)
                    if(response.isSuccessful && response.code() == 200){
                        getCartItems()
//                        _cartItem.postValue(response.body())
                    }else{
//                        _cartItem.postValue(null)
                    }
                }
            }catch (e: Exception){
                println(e)
            }
        }
    }

    fun removeProductFromCart(productID: String, sizeID: String){
        viewModelScope.launch {
            try{
                val customerID = UserSesion.currentUser?.id
                if(!customerID.isNullOrBlank()){
                    val response = apiService.removeProductFromCart(customerID, productID, sizeID)
                    if(response.isSuccessful && response.code() == 200){
                        getCartItems()
//                        _cartItem.postValue(response.body())
                    }else{
//                        _cartItem.postValue(null)
                    }
                }
            }catch (e: Exception){
                println(e)
            }
        }
    }

    fun removeSelectedItems(selectedItems: List<CartItem>) {
        viewModelScope.launch {
            try {
                val customerID = UserSesion.currentUser?.id
                if (!customerID.isNullOrBlank()) {
                    selectedItems.forEach { item ->
                        apiService.removeProductFromCart(customerID, item.productId, item.sizeID)
                    }
                    getCartItems() // Cập nhật giỏ hàng sau khi xóa sản phẩm
                }
            } catch (e: Exception) {
                println(e)
            }
        }
    }
}