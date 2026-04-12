package ru.topbun.upload.di

import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.topbun.upload.UploadViewModel

val uploadModule = module {
    viewModelOf(::UploadViewModel)
}