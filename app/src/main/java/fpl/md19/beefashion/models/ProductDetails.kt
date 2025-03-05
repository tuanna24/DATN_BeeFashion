package fpl.md19.beefashion.models

//import kotlinx.coroutines.internal.OpDescriptor

data class ProductDetails(
    val id : String,
    val name : String,
    val productId : String,
    val quantities : List<Int>,
    val description: String,
    val managerId : String,
    val images: List<String>,
    val price : Int,
    val sizeId : List<String>,
    val brandId : String,
    val color : String,
    val sizes : List<Sizes>
)
