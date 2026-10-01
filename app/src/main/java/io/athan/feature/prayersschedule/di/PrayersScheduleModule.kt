package io.athan.feature.prayersschedule.di

import io.athan.feature.prayersschedule.data.remote.AladhanApiService
import io.athan.feature.prayersschedule.data.repository.PrayersScheduleRepositoryImpl
import io.athan.feature.prayersschedule.domain.usecase.GetPrayerTimesUseCase
import io.athan.feature.prayersschedule.domain.repository.PrayersScheduleRepository
import io.athan.feature.prayersschedule.presentation.PrayersScheduleViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val prayersScheduleModule = module {
    // APIs
    singleOf(::AladhanApiService)

    // repositories
    singleOf(::PrayersScheduleRepositoryImpl) bind PrayersScheduleRepository::class

    // view models
    viewModelOf(::PrayersScheduleViewModel)

    // usecases
    factoryOf(::GetPrayerTimesUseCase)
}