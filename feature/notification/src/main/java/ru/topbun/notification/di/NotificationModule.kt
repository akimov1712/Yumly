package ru.topbun.notification.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.topbun.notification.NotificationViewModel

val notificationModule = module {
    viewModelOf(::NotificationViewModel)
}
