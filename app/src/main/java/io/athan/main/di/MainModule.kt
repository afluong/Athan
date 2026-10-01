package io.athan.main.di

import io.athan.main.MainViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val mainModule = module {

    viewModelOf(::MainViewModel)
}