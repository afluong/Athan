package io.athan.di

import androidx.room.Room
import io.athan.core.database.AthanDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val roomDatabaseModule = module {

    single {
        Room.databaseBuilder(
            androidContext(),
            AthanDatabase::class.java,
            "athan_app.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    single { get<AthanDatabase>().locationDao() }
}