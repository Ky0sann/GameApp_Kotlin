package fr.sdv.b3dev.gameapp.presentation

enum class SortOption(val displayName: String) {
    NAME_ASC("Name ↑"),
    NAME_DESC("Name ↓"),
    RATING_ASC("Rating ↑"),
    RATING_DESC("Rating ↓"),
    RELEASED_ASC("Release Date ↑"),
    RELEASED_DESC("Release Date ↓")
}

fun SortOption.toApiOrdering(): String = when(this) {
    SortOption.NAME_ASC -> "name"
    SortOption.NAME_DESC -> "-name"
    SortOption.RATING_ASC -> "rating"
    SortOption.RATING_DESC -> "-rating"
    SortOption.RELEASED_ASC -> "released"
    SortOption.RELEASED_DESC -> "-released"
}