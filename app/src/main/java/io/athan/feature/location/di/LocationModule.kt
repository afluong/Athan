package io.athan.feature.location.di

import io.athan.feature.location.data.repository.LocationRepositoryImpl
import io.athan.feature.location.data.remote.api.PhotonApiService
import io.athan.feature.location.domain.usecase.GetSavedLocationUseCase
import io.athan.feature.location.domain.repository.LocationRepository
import io.athan.feature.location.domain.usecase.LocationSearchUseCase
import io.athan.feature.location.domain.usecase.SaveLocationUseCase
import io.athan.feature.location.presentation.LocationSearchViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val locationModule = module {
    // APIs
    singleOf(::PhotonApiService)

    // repositories
    singleOf(::LocationRepositoryImpl) bind LocationRepository::class

    // view models
    viewModelOf(::LocationSearchViewModel)

    // use cases
    factoryOf(::LocationSearchUseCase)
    factoryOf(::SaveLocationUseCase)
    factoryOf(::GetSavedLocationUseCase)
}