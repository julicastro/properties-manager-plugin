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
            val file = file("updatePlugins.xml")
            var content = file.readText()
            
            // Actualizar URL del plugin
            content = content.replace(
                Regex("""url="[^"]*v[\d.]+/properties-manager-plugin-[\d.]+\.zip""""),
                """url="https://github.com/julianemanue_meli/properties-manager-plugin/releases/download/v$version/properties-manager-plugin-$version.zip""""
            )
            
            // Actualizar atributo version en el tag <plugin>
            content = content.replace(
                Regex("""<plugin id="com\.properties\.manager"[^>]*version="[\d.]+" """),
                """<plugin id="com.properties.manager" url="https://github.com/julianemanue_meli/properties-manager-plugin/releases/download/v$version/properties-manager-plugin-$version.zip" version="$version" """
            )
            
            file.writeText(content)
            println("✅ updatePlugins.xml actualizado a versión $version")
        }
    }
}

