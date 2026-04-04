package ru.topbun.home_filter.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.topbun.home_filter.HomeFilterViewModel

val homeFilterModule = module {
    viewModelOf(::HomeFilterViewModel)
}