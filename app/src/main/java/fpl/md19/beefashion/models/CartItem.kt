package fpl.md19.beefashion.models

data class CartItem(
    val id: Int,
    val name: String,
    val size: String,
    val price: Int,
    val image: Int,
    var quantity: Int
)
