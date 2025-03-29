package fpl.md19.beefashion.requests

data class AddressRequest(
    val province: String,
    val district: String,
    val ward: String,
    val detail: String,
    val name: String,
    val phoneNumber: String
)