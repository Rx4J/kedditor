package ru.lanik.network.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import ru.lanik.network.api.SubredditsAPI
import ru.lanik.network.api.interceptor.LoggingInterceptorQualifier
import ru.lanik.network.constants.ApiBaseConst
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ApiModule {
    private val format = Json {
        ignoreUnknownKeys = true
    }

    @Provides
    @LoggingInterceptorQualifier
    fun provideLoginInterceptor(): Interceptor =
        HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    @Singleton
    fun provideMediaType(): MediaType = "application/json".toMediaType()

    @Provides
    @Singleton
    fun provideRedditClient(
        @LoggingInterceptorQualifier loggingInterceptor: Interceptor,
    ): OkHttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor(loggingInterceptor)
        .followRedirects(true)
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(okhttpClient: OkHttpClient, type: MediaType): Retrofit = Retrofit.Builder()
        .baseUrl(ApiBaseConst.BASE_URL.notAuth)
        .addConverterFactory(format.asConverterFactory(type))
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .client(okhttpClient)
        .build()

    @Provides
    @Singleton
    fun provideApiSubreddits(retrofit: Retrofit): SubredditsAPI =
        retrofit.create(SubredditsAPI::class.java)
}