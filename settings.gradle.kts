rootProject.name = "soundbound-extensions-lib"

dependencyResolutionManagement {
    versionCatalogs {
        create("deps") {
            from(files("gradle/deps.versions.toml"))
        }
    }
}
include(":compose")
include(":api")
//include(":parcelize")
