package fr.sdv.b3dev.gameapp.di

import fr.sdv.b3dev.gameapp.datasource.rest.ApiModule
import fr.sdv.b3dev.gameapp.datasource.rest.FavoritesFileManager
import fr.sdv.b3dev.gameapp.datasource.rest.FavoritesRepository
import fr.sdv.b3dev.gameapp.datasource.rest.GameRemoteDataSource
import fr.sdv.b3dev.gameapp.presentation.GameRepository
import fr.sdv.b3dev.gameapp.presentation.GameListViewModel
import fr.sdv.b3dev.gameapp.presentation.GameDetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single { ApiModule.api }

    single { GameRemoteDataSource(get()) }

    single { GameRepository(get()) }

    single { FavoritesFileManager(androidContext()) }

    // 🔹 Favorites repository needs API repo + file manager
    single { FavoritesRepository(get(), get()) }


    viewModel { GameListViewModel(get(), get(), get()) }

    viewModel { GameDetailViewModel(get()) }
}
