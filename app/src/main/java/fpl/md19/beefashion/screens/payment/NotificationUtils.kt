package fpl.md19.beefashion.screens.payment

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import fpl.md19.beefashion.R

object NotificationUtils {

    private const val CHANNEL_ID = "order_channel_id"
    private const val CHANNEL_NAME = "BeeFashion Channel"
    private const val NOTIFICATION_ID = 1

    fun showOrderSuccessNotification(context: Context) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Kênh thông báo đặt hàng thành công"
            }
            notificationManager.createNotificationChannel(channel)
        }

        // Tạo thông báo
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.bell)
            .setContentTitle("BeeFashion")
            .setContentText("Bạn đã đặt hàng thành công!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }
}