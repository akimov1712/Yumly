package ru.topbun.home.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.topbun.home.HomeViewModel

val homeModule = module {
    viewModelOf(::HomeViewModel)
}