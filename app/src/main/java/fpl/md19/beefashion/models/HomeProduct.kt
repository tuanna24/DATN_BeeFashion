package fpl.md19.beefashion.models

data class HomeProduct (
    val title : String,
    val price : Int,
    val imageRes : Int,
    val size : String,
    val discount: String? = null
)