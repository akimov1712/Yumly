package ru.topbun.yumly

import android.app.Application
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.screen.Screen
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import ru.topbun.assistant.AssistantScreen
import ru.topbun.auth.AuthScreen
import ru.topbun.bmi.BmiScreen
import ru.topbun.auth_confirm.ConfirmScreen
import ru.topbun.auth_login.LoginScreen
import ru.topbun.auth_register.RegisterScreen
import ru.topbun.auth_reset.ResetScreen
import ru.topbun.auth_reset_request.ResetRequestScreen
import ru.topbun.auth_welcome.WelcomeScreen
import ru.topbun.dashboard.DashboardScreen
import ru.topbun.feature.splash.SplashScreen
import ru.topbun.home.HomeScreen
import ru.topbun.navigation.BmiScreenProvider
import ru.topbun.navigation.DashboardScreenProvider
import ru.topbun.navigation.ProfileScreenProvider
import ru.topbun.navigation.RecipeScreenProvider
import ru.topbun.navigation.RootScreenProvider
import ru.topbun.navigation.auth.AuthScreenProvider
import ru.topbun.notification.NotificationScreen
import ru.topbun.profile.ProfileScreen
import ru.topbun.profile.ProfileScreenContent
import ru.topbun.profile_followers.FollowListScreen
import ru.topbun.profile_settings.ProfileSettingsScreen
import ru.topbun.recipe.RecipeScreen
import ru.topbun.upload.UploadScreen
import ru.topbun.yumly.di.appModule

class App : Application() {

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


    private fun initScreens() {
        ScreenRegistry {
            register<RootScreenProvider.Splash> { SplashScreen }
            register<RootScreenProvider.Auth> { AuthScreen(it.startScreen) }
            register<RootScreenProvider.Dashboard> { DashboardScreen }

            register<AuthScreenProvider.Welcome> { WelcomeScreen }
            register<AuthScreenProvider.Login> { LoginScreen }
            register<AuthScreenProvider.Register> { RegisterScreen }
            register<AuthScreenProvider.ResetRequest> { ResetRequestScreen }
            register<AuthScreenProvider.Confirm> { ConfirmScreen(it.email, it.screenMode) }
            register<AuthScreenProvider.Reset>{ ResetScreen(it.email) }

            register<DashboardScreenProvider.Home> { HomeScreen as Screen }
            register<DashboardScreenProvider.Upload> { UploadScreen as Screen }
            register<DashboardScreenProvider.Assistant> { AssistantScreen as Screen }
            register<DashboardScreenProvider.Notification> { NotificationScreen as Screen }
            register<DashboardScreenProvider.Profile> { ProfileScreen as Screen }

            register<ProfileScreenProvider.User> { ProfileScreenContent(it.userId) }
            register<ProfileScreenProvider.Settings> { ProfileSettingsScreen }
            register<ProfileScreenProvider.Follows> { FollowListScreen(it.userId, it.initialTab) }

            register<RecipeScreenProvider.Detail> { RecipeScreen(it.recipeId) }

            register<BmiScreenProvider.Main> { BmiScreen }

        }
    }

}
