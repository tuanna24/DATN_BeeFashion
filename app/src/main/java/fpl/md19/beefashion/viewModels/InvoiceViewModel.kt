package fpl.md19.beefashion.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fpl.md19.beefashion.GlobalVarible.UserSesion
import fpl.md19.beefashion.api.ApiService
import fpl.md19.beefashion.api.HttpRequest
import fpl.md19.beefashion.models.MyOder
import kotlinx.coroutines.launch

class InvoiceViewModel: ViewModel() {
    private val apiService: ApiService = HttpRequest.getInstance()

    private val _invoices = MutableLiveData<List<MyOder>>()
    val invoices: LiveData<List<MyOder>> = _invoices

    fun getCustomerInvoices(){
        viewModelScope.launch {
            try{
                val customerID = UserSesion.currentUser?.id
                if(!customerID.isNullOrBlank()){
                    val invoiceRes = apiService.getInvoices(customerID)
                    if(invoiceRes.isSuccessful && invoiceRes.code() == 200){
                        _invoices.postValue(invoiceRes.body())
                    }
                }
            }catch (e: Exception){
                println(e)
            }
        }
    }

    fun newCustomerInvoices(newInvoice: MyOder){
        viewModelScope.launch {
            try{
                val customerID = UserSesion.currentUser?.id
                if(!customerID.isNullOrBlank()){
                    val invoiceRes = apiService.makeAnInvoice(newInvoice)
                    println(invoiceRes)
//                    if(invoiceRes.isSuccessful && invoiceRes.code() == 200){
//                        _invoices.postValue(invoiceRes.body())
//                    }
                }
            }catch (e: Exception){
                println(e)
            }
        }
    }

    fun cancelCustomerInvoice(invoiceID: String){
        viewModelScope.launch {
            try{
                val customerID = UserSesion.currentUser?.id
                if(!customerID.isNullOrBlank()){
                    val invoiceRes = apiService.cancelInvoice(customerID, invoiceID)
                    if(invoiceRes.isSuccessful && invoiceRes.code() == 200){
//                        _invoices.postValue(invoiceRes.body())
                    }
                }
            }catch (e: Exception){
                println(e)
            }
        }
    }

    fun completeCustomerInvoice(invoiceID: String){
        viewModelScope.launch {
            try{
                val customerID = UserSesion.currentUser?.id
                if(!customerID.isNullOrBlank()){
                    val invoiceRes = apiService.completeInvoice(customerID, invoiceID)
                    if(invoiceRes.isSuccessful && invoiceRes.code() == 200){
//                        _invoices.postValue(invoiceRes.body())
                    }
                }
            }catch (e: Exception){
                println(e)
            }
        }
    }
}