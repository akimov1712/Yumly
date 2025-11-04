package ru.topbun.yumly

import android.app.Application
import cafe.adriel.voyager.core.registry.ScreenRegistry
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import ru.topbun.feature.splash.di.splashScreenModule
import ru.topbun.yumly.di.appModule

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
        initScreens()
    }


    private fun initKoin() {
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(appModule)
        }
    }


    private fun initScreens(){
        ScreenRegistry{
            splashScreenModule
        }
    }

}