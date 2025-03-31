package fpl.md19.beefashion.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fpl.md19.beefashion.GlobalVarible.UserSesion
import fpl.md19.beefashion.api.ApiService
import fpl.md19.beefashion.api.HttpRequest
import fpl.md19.beefashion.models.Products
import kotlinx.coroutines.launch

class FavoriteViewModel: ViewModel() {
    private val apiService: ApiService = HttpRequest.getInstance()

    private var _products = MutableLiveData<List<Products>?>()
    var products: LiveData<List<Products>?> = _products

    fun getFavoriteProducts(){
        viewModelScope.launch {
            try{
                val customerID = UserSesion.currentUser?.id
                if(!customerID.isNullOrBlank()){
                    val favProductsRes = apiService.getFavoriteProducts(customerID)
                    if(favProductsRes.isSuccessful && favProductsRes.code() == 200){
                        _products.postValue(favProductsRes.body())
                    }
                }
            }catch (e: Exception){
                println(e)
            }
        }
    }

    fun addFavoriteProducts(productID: String){
        println("adding fav")
        viewModelScope.launch {
            try{
                val customerID = UserSesion.currentUser?.id
                if(!customerID.isNullOrBlank()){
                    val favProductsRes = apiService.addFavoriteProduct(customerID, productID)
                    if(favProductsRes.isSuccessful && favProductsRes.code() == 200){
//                        _products.postValue(favProductsRes.body())
                        println("added fav")
                    }
                }
            }catch (e: Exception){
                println(e)
            }
        }
    }

    fun removeFavoriteProducts(productID: String){
        println("deleting fav")
        viewModelScope.launch {
            try{
                val customerID = UserSesion.currentUser?.id
                if(!customerID.isNullOrBlank()){
                    val favProductsRes = apiService.removeFavoriteProduct(customerID, productID)
                    if(favProductsRes.isSuccessful && favProductsRes.code() == 200){
//                        _products.postValue(favProductsRes.body())
                        println("deleted fav")
                    }
                }
            }catch (e: Exception){
                println(e)
            }
        }
    }

}