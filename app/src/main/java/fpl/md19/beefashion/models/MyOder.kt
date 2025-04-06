package fpl.md19.beefashion.models

import fpl.md19.beefashion.screens.cart.OrderStatus

data class MyOder(
    val title : String,
    val size : String,
    val price : String,
    val imageRes : Int,
    val status: OrderStatus
)
