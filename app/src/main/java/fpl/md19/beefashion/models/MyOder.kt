package fpl.md19.beefashion.models

import fpl.md19.beefashion.screens.cart.OrderStatus

data class MyOder(
    var id: String? = null,
    var customerID: String,
    val addressID: String,
    val paidStatus: Boolean,
    val invoiceItemDTOs: List<OrderItem>,
    val paymentMethod: String,
    val total: Int? = null,
    val status: String? = null,
    val createdAt: String? = null
)

data class OrderItem(
    val productID: String,
    val sizeID: String,
    val quantity: Int,
    val sizeName: String,
    val productName: String,
    val productImage: String,
    val productPrice: Int,
    val product: Products? = null,
    val size: Sizes? = null
)