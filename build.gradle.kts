plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.22"
    id("org.jetbrains.intellij") version "1.16.1"
}

group = providers.gradleProperty("pluginGroup").get()
version = providers.gradleProperty("pluginVersion").get()

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
}

intellij {
    version.set(providers.gradleProperty("platformVersion").get())
    type.set(providers.gradleProperty("platformType").get())
    plugins.set(listOf())
}

tasks {
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    patchPluginXml {
        sinceBuild.set("232")
        untilBuild.set("")
    }
    
    buildSearchableOptions {
        enabled = false
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
    
    register("updatePluginsXml") {
        doLast {
            val version = project.version.toString()
            val githubUsername = providers.gradleProperty("githubUsername").get()
            val pluginName = providers.gradleProperty("pluginName").get()
            val file = file("updatePlugins.xml")
            var content = file.readText()
            
            val repoUrl = "https://github.com/$githubUsername/$pluginName"
            val downloadUrl = "$repoUrl/releases/download/v$version/$pluginName-$version.zip"
            
            // Actualizar URL del plugin
            content = content.replace(
                Regex("""url="https://github\.com/[^/]+/[^/]+/releases/download/v[\d.]+/[^"]+""""),
                """url="$downloadUrl""""
            )
            
            // Actualizar vendor url
            content = content.replace(
                Regex("""<vendor[^>]*url="https://github\.com/[^/]+/[^"]+" """),
                """<vendor email="julianemanuel.castro@mercadolibre.com" url="$repoUrl" """
            )
            
            file.writeText(content)
            println("✅ updatePlugins.xml actualizado a versión $version")
            println("   URL: $downloadUrl")
        }
    }
}

