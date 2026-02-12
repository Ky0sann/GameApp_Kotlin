package fr.sdv.b3dev.gameapp.domain

import com.google.gson.annotations.SerializedName

data class GameRemoteDetailResponse(
    val id: Int,
    val name: String,
    @SerializedName("background_image") val backgroundImage: String?,
    val rating: Double,
    val released: String?,
    val description_raw: String?,
    val metacritic: Int?,
    val website: String?,
    val genres: List<GenreResponse> = emptyList(),
    val platforms: List<PlatformResponse> = emptyList(),
    val developers: List<CompanyResponse> = emptyList(),
    val publishers: List<CompanyResponse> = emptyList(),
    val tags: List<TagResponse> = emptyList(),
    val esrb_rating: EsrbResponse?
)

data class GenreResponse(
    val name: String
)

data class PlatformResponse(
    val platform: PlatformDetail
)

data class PlatformDetail(
    val name: String
)