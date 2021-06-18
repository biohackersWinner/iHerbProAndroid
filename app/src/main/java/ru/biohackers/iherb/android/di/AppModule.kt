package ru.biohackers.iherb.android.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.biohackers.iherb.android.BuildConfig
import ru.biohackers.iherb.android.data.AuthTokenInterceptor
import ru.biohackers.iherb.android.data.api.ApiService
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthTokenInterceptor,
        // baseHeadersInterceptor: BaseHeadersInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(authInterceptor)
            // .addInterceptor(baseHeadersInterceptor)
            .applyLoggingInterceptor()
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(
        client: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            // .addCallAdapterFactory(ApiCallAdapterFactory.create(responseTransformer))
            .build()

    @Provides
    @Singleton
    fun provideApi(
        retrofit: Retrofit
    ) = retrofit.create(ApiService::class.java)

    private fun OkHttpClient.Builder.applyLoggingInterceptor(): OkHttpClient.Builder = apply {
        if (BuildConfig.DEBUG) {
            addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        }
    }
}
