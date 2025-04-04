package fpl.md19.beefashion.screens.data

import fpl.md19.beefashion.models.OrderStatusStepModel

val orderStatusList = listOf(
    OrderStatusStepModel("Đang chờ xác nhận", "2336 Van Thang, Ba bi, Ha noi", completed = true),
    OrderStatusStepModel("Đã xác nhận đơn hàng", "2336 Van Thang, Ba bi, Ha noi", completed = true),
    OrderStatusStepModel("Đã lấy hàng", "2417 Hoan Kiem, Ba Dinh, Ha noi", completed = true),
    OrderStatusStepModel("Đang vận chuyển", "16 Nhon, Xuan Phuong, Ha noi", completed = false),
    OrderStatusStepModel("Đã giao hàng", "925 Nhon, Ha Noi", completed = false)
)