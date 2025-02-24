package fpl.md19.beefashion.requests

data class RegisterRequest(
    val fullName: String,
    val email: String,
    val password: String
)