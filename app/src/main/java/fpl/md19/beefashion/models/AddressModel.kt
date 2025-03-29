package fpl.md19.beefashion.models

data class AddressModel(
    val id: String,
    val province: String,
    val district: String,
    val ward: String,
    val detail: String,
    val name: String,
    val phoneNumber : String
)
