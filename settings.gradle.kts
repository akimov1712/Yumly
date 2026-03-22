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
