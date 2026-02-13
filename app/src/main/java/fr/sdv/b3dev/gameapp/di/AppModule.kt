package fr.sdv.b3dev.gameapp.di

import fr.sdv.b3dev.gameapp.datasource.rest.ApiModule
import fr.sdv.b3dev.gameapp.datasource.rest.GameRemoteDataSource
import fr.sdv.b3dev.gameapp.presentation.GameRepository
import fr.sdv.b3dev.gameapp.presentation.GameListViewModel
import fr.sdv.b3dev.gameapp.presentation.GameDetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.koin.android.ext.koin.androidContext

val appModule = module {

    single { ApiModule.provideApi(androidContext()) }

    single { GameRemoteDataSource(get()) }

    single { GameRepository(get()) }

    viewModel { GameListViewModel(get()) }

    viewModel { GameDetailViewModel(get()) }
}
