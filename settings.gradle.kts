rootProject.name = "GurkhaHR"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

include(":composeApp")
include(":core:network")
include(":core:persistance:datastore")
include(":core:di")
include(":core:data")
include(":core:domain")
include(":core:model")
include(":core:networkHelper")


include(":features:auth:login")
include(":features:dashboard")
include(":core:ui:res")
include(":core:ui:components")
include(":core:crypto")
include(":features:splashscreen")
include(":features:home")
include(":features:attendance")
include(":features:profile")
