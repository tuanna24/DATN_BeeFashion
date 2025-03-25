//package fpl.md19.beefashion.api.Zalopay
//
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext
//import okhttp3.FormBody
//import okhttp3.RequestBody
//import org.json.JSONObject
//
//class CreateOrder {
//
//    @Throws(Exception::class)
//    suspend fun createOrder(amount: String): JSONObject {
//        println("Lon")
//        return withContext(Dispatchers.IO) {
//            val input = ZaloPayOrder(amount = amount)
//            val formBody: RequestBody = FormBody.Builder()
//                .add("app_id", input.appId)
//                .add("app_user", input.appUser)
//                .add("app_time", input.appTime)
//                .add("amount", input.amount)
//                .add("app_trans_id", input.appTransId)
//                .add("embed_data", input.embedData)
//                .add("item", input.items)
//                .add("bank_code", input.bankCode)
//                .add("description", input.description)
//                .add("mac", input.mac)
////                .add("return_code", input.)
//                .build()
//
//            HttpProvider.sendPost(url = AppInfo.URL_CREATE_ORDER, formBody =  formBody)
//        } ?: throw Exception("Failed to create order")
//    }
//}