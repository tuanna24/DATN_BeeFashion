package fpl.md19.beefashion.models

data class OrderStatusStepModel(
    val status: String,
    val address: String,
    val completed: Boolean
)