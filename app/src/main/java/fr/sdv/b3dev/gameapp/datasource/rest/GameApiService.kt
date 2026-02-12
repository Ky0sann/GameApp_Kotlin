package fr.sdv.b3dev.gameapp.datasource.rest

import fr.sdv.b3dev.gameapp.domain.GameRemoteDetailResponse
import fr.sdv.b3dev.gameapp.domain.GameResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Path
import fr.sdv.b3dev.gameapp.domain.ScreenshotResponse

interface GameApiService {

    @GET("games")
    suspend fun getPopularGames(
        @Query("key") apiKey: String,
        @Query("ordering") ordering: String = "-rating",
        @Query("page_size") pageSize: Int = 20
    ): GameResponse

    @GET("games/{id}")
    suspend fun getGameDetail(
        @Path("id") gameId: Int,
        @Query("key") apiKey: String
    ): GameRemoteDetailResponse

    @GET("games/{id}/screenshots")
    suspend fun getGameScreenshots(
        @Path("id") gameId: Int,
        @Query("key") apiKey: String
    ): ScreenshotResponse
}
