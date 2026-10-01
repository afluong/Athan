package io.athan.di

import io.athan.core.network.networkModule
import io.athan.feature.location.di.locationModule
import io.athan.feature.prayersschedule.di.prayersScheduleModule
import io.athan.main.di.mainModule
import org.koin.dsl.module

val appModule = module {
    includes(
        networkModule,
        roomDatabaseModule,
        mainModule,
        locationModule,
        prayersScheduleModule
    )
}