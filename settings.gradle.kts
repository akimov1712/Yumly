pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
        maven {
            url = uri("https://artifactory-external.vkpartner.ru/artifactory/maven")
        }
    }
}

rootProject.name = "Yumly"
include(":app")
include(":domain")
include(":core")
include(":core:common")
include(":data")
include(":core:android")
include(":core:ui")
include(":feature")
include(":feature:splash")
include(":navigation")
include(":feature:auth")
include(":feature:auth_welcome")
include(":feature:auth_login")
include(":feature:auth_register")
include(":feature:auth_confirm")
include(":feature:auth_reset_request")
include(":feature:dashboard")
include(":feature:auth_reset")
include(":feature:home")
include(":feature:upload")
include(":feature:assistant")
include(":feature:notification")
include(":feature:profile")
include(":feature:home_filter")
include(":feature:profile_settings")
include(":feature:recipe")
include(":feature:profile_followers")
include(":feature:bmi")
