package fpl.md19.beefashion.requests

import okhttp3.MultipartBody

data class UpdateUserRequest(
    val fullName: String,
    val email: String,
    val dateOfBirth: String?,
    val gender: String?,
    val phone: String?,
    val image: MultipartBody.Part?
)