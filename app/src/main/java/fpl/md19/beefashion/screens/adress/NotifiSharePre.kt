package fpl.md19.beefashion.screens.adress

import android.content.Context
import androidx.lifecycle.ViewModel


class NotifiSharePre : ViewModel() {

    fun shouldNotify(orderId: String, newStatus: String?, context: Context): Boolean {
        val prefs = context.getSharedPreferences("notified_order_statuses", Context.MODE_PRIVATE)
        val lastStatus = prefs.getString(orderId, null)
        val shouldNotify = newStatus != null && newStatus != lastStatus
        if (shouldNotify) {
            prefs.edit().putString(orderId, newStatus).apply()
        }
        return shouldNotify
    }
}
