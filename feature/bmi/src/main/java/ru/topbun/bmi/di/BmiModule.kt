package ru.topbun.bmi.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.topbun.bmi.BmiViewModel

val bmiModule = module {
    viewModelOf(::BmiViewModel)
}
