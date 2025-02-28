package fpl.md19.beefashion.models

data class UserModel (
    val id: String,
    val fullName: String,
    val email: String,
    val dateOfBirth: String?,
    val gender: String?,
    val phone: String?,
    val profileImageUrl: String? = null
)