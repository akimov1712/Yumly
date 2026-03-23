package ru.topbun.yumly

import android.app.Application
import cafe.adriel.voyager.core.registry.ScreenRegistry
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import ru.topbun.feature.auth.AuthScreen
import ru.topbun.feature.auth.welcome.WelcomeScreen
import ru.topbun.feature.splash.SplashScreen
import ru.topbun.navigation.RootScreenProvider
import ru.topbun.navigation.auth.AuthScreenProvider
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
            register<RootScreenProvider.Splash>{ SplashScreen }
            register<RootScreenProvider.Auth>{ AuthScreen(it.startScreen) }

            register<AuthScreenProvider.Welcome>{ WelcomeScreen }
//            register<AuthScreenProvider.Login>{ LoginScreen }
//            register<AuthScreenProvider.Register>{ RegisterScreen }
//            register<AuthScreenProvider.Confirm>{ ConfirmScreen(it.email, it.screenMode) }
//            register<AuthScreenProvider.ResetRequest>{ ResetRequestScreen }
//            register<AuthScreenProvider.ResetNewPassword>{ ResetPasswordScreen(it.email, it.code) }

        }
    }

}
