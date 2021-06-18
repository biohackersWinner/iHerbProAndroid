package ru.biohackers.iherb.android.data

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthTokenInterceptor @Inject constructor(
    private val preferenceManager: PreferenceManager,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()

        val token = preferenceManager.getToken()
        if (!token.isNullOrEmpty()) {
            val authorization = makeAuthorizationHeaderValue(token)
            builder.header(AUTHORIZATION, authorization)
        }
        return chain.proceed(builder.build())
    }

    companion object {
        const val AUTHORIZATION = "Authorization"
        fun makeAuthorizationHeaderValue(accessToken: String) = "Bearer $accessToken"
    }

}