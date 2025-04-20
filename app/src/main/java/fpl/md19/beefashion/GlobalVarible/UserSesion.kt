package fpl.md19.beefashion.GlobalVarible

import fpl.md19.beefashion.models.AddressModel
import fpl.md19.beefashion.models.MyOder
import fpl.md19.beefashion.models.OrderItem
import fpl.md19.beefashion.models.UserModel

object UserSesion {
    var currentUser: UserModel? = null
    var userOrderItems: List<OrderItem> = emptyList()
    var userSelectedAddress: AddressModel? = null
    var selectedOrder: MyOder? = null
}