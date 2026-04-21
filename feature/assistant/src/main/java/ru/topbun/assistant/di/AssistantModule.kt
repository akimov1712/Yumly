package ru.topbun.assistant.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.topbun.assistant.AssistantViewModel

val assistantModule = module {
    viewModelOf(::AssistantViewModel)
}
