package fr.sdv.b3dev.gameapp.di

import androidx.room.Room
import fr.sdv.b3dev.gameapp.datasource.local.AppDatabase
import fr.sdv.b3dev.gameapp.datasource.local.UserDao
import fr.sdv.b3dev.gameapp.datasource.rest.ApiModule
import fr.sdv.b3dev.gameapp.datasource.rest.GameRemoteDataSource
import fr.sdv.b3dev.gameapp.presentation.*
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    // ========================
    // ✅ ROOM DATABASE
    // ========================

    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "gameapp_db"
        ).build()
    }

    single<UserDao> {
        get<AppDatabase>().userDao()
    }

    // ========================
    // ✅ USER REPOSITORY
    // ========================

    single { UserRepository(get()) }

    viewModel { AuthViewModel(get()) }

    // ========================
    // ✅ RAWG API
    // ========================

    single { ApiModule.provideApi(androidContext()) }

    single { GameRemoteDataSource(get()) }

    single { GameRepository(get()) }

    viewModel { GameListViewModel(get()) }

    viewModel { GameDetailViewModel(get()) }
}
