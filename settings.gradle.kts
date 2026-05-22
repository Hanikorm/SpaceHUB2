pluginManagement {

    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        // Если ты скачал WorldWind вручную в папку libs, добавь:
        flatDir {
            dirs("libs")
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral() // Ставим его выше
        google()
        maven { url = uri("https://jitpack.io") } // Проверь, чтобы не было опечаток в uri
    }
}

rootProject.name = "SpaceHUB2"
include(":app")
