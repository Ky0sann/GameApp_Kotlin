package fr.sdv.b3dev.gameapp.datasource.rest

import fr.sdv.b3dev.gameapp.domain.GameResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GameApiService {

    @GET("games")
    suspend fun getPopularGames(
        @Query("key") apiKey: String,
        @Query("ordering") ordering: String = "-rating",
        @Query("page_size") pageSize: Int = 20
    ): GameResponse
}
