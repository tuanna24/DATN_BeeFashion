package fpl.md19.beefashion.screens.data

import fpl.md19.beefashion.models.OrderStatusStepModel

val orderStatusList = listOf(
    OrderStatusStepModel("Đang chờ xác nhận", "", completed = true),
    OrderStatusStepModel("Đã xác nhận đơn hàng", "", completed = true),
    OrderStatusStepModel("Đã lấy hàng", "", completed = false),
    OrderStatusStepModel("Đang vận chuyển", "", completed = false),
    OrderStatusStepModel("Đã giao hàng", "", completed = false)
)