package fpl.md19.beefashion.models

data class CartItem(
    val id: String,
    val productId: String,
    val sizeID: String,
    val size: Sizes,
    val product: Products,
    var quantity: Int
)

data class CartItemSentData(
    val sizeID: String,
    val productId: String,
    val quantity: Int
)