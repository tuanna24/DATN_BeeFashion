package fpl.md19.beefashion.screens.adress

import android.content.Context

class AddressPreferenceManager(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("address_prefs", Context.MODE_PRIVATE)

    fun saveSelectedAddress(address: String) {
        sharedPreferences.edit().putString("selected_address", address).apply()
    }

    fun getSelectedAddress(): String? {
        return sharedPreferences.getString("selected_address", null)
    }
}
