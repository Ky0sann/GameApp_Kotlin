package fr.sdv.b3dev.gameapp.datasource.rest

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiModule {

    private const val BASE_URL = "https://api.rawg.io/api/"

    val api: GameApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GameApiService::class.java)
    }
}
