package fr.sdv.b3dev.gameapp.datasource.rest

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.Cache
import okhttp3.OkHttpClient
import java.io.File
import android.content.Context

object ApiModule {

    private const val BASE_URL = "https://api.rawg.io/api/"

    fun provideApi(context: Context): GameApiService {

        val cacheSize = 10L * 1024 * 1024 // 10MB
        val cache = Cache(File(context.cacheDir, "http_cache"), cacheSize)

        val client = OkHttpClient.Builder()
            .cache(cache)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GameApiService::class.java)
    }
}
