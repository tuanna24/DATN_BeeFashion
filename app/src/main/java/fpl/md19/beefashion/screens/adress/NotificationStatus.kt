package fpl.md19.beefashion.screens.adress

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import fpl.md19.beefashion.R
object NotificationStatus {

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "OrderStatusChannel"
            val descriptionText = "Thông báo trạng thái đơn hàng"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("order_status", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun sendOrderStatusNotification(context: Context, orderId: String, status: String) {
        val builder = NotificationCompat.Builder(context, "order_status")
            .setSmallIcon(R.drawable.bell)
            .setContentTitle("Cập nhật đơn hàng")
            .setContentText("Đơn hàng #OD_$orderId hiện đang ở trạng thái: $status")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(orderId.hashCode(), builder.build())
        }
    }

    fun sendOrderStatusNotification1(context: Context, orderId: String, status: String) {
        val statusMessage = when (status.lowercase()) {
            "pending" -> "Đang chờ xác nhận"
            "packing" -> "Đã đóng gói đơn hàng"
            "intransit" -> "Đang vận chuyển"
            "completed" -> "Đã giao hàng"
            else -> "Trạng thái không xác định"
        }

        val builder = NotificationCompat.Builder(context, "order_status")
            .setSmallIcon(R.drawable.bell)
            .setContentTitle("Cập nhật đơn hàng")
            .setContentText("Đơn hàng #OD_$orderId hiện đang ở trạng thái: $statusMessage")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(orderId.hashCode(), builder.build())
        }

    }
}
