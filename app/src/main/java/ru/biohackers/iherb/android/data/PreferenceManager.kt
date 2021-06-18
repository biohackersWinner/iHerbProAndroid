package ru.biohackers.iherb.android.data

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PreferenceManager @Inject constructor(@ApplicationContext context: Context) {

    private var sharedPreferences = context.getSharedPreferences("Preferences", Context.MODE_PRIVATE)

    fun setToken(token: String) {
        sharedPreferences.edit().putString(TOKEN, token).apply()
    }

    fun getToken(): String? =
        sharedPreferences.getString(TOKEN, null)

    fun resetToken() {
        sharedPreferences.edit().remove(TOKEN).apply()
    }

    companion object {
        const val TOKEN = "TOKEN"
    }
}
