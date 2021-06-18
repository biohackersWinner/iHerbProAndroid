package ru.biohackers.iherb.android.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.biohackers.iherb.android.data.api.ApiError.Companion.parseAsError
import ru.biohackers.iherb.android.data.api.ApiService
import javax.inject.Inject

class Repository @Inject constructor(
    private val preferenceManager: PreferenceManager,
    private val apiService: ApiService,
) {

    suspend fun apiCall(param: String): Result<String> = withContext(Dispatchers.IO) {
        val response = apiService.get()
        if (response.isSuccessful) {
            Result.success(response.body()!!.url)
        } else {
            Result.failure(
                Error(response.errorBody()?.parseAsError()?.message ?: "Неизвестная ошибка")
            )
        }
    }

    fun isAuthorized(): Boolean {
        return true
    }
}

