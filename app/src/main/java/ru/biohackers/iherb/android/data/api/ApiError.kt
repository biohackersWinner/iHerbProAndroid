package ru.biohackers.iherb.android.data.api

import com.google.gson.Gson
import okhttp3.ResponseBody

data class ApiError(
    val code: Int?,
    val status: String?,
    val error: String?,
) {
    companion object {
        fun ResponseBody.parseAsError(): ApiError? {
            return Gson().fromJson(this.string(), ApiError::class.java)
        }
    }
}