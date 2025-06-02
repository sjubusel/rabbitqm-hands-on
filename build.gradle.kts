plugins {
    // Apply the shared build logic from a convention plugin.
    // The shared code is located in `buildSrc/src/main/kotlin/kotlin-jvm.gradle.kts`.
    id("buildsrc.convention.kotlin-jvm")
}

subprojects {
    apply(plugin = "buildsrc.convention.kotlin-jvm")

    dependencies {
        implementation(rootProject.libs.rabbit.mq.client)
        implementation(rootProject.libs.bundles.slf4j)
    }
}
