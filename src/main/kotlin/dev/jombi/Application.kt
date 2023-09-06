package dev.jombi

import dev.jombi.database.DatabaseFactory
import dev.jombi.plugins.*
import io.ktor.network.tls.certificates.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.serialization.json.Json
import java.io.File
import java.security.KeyStore
import kotlin.io.path.Path
import kotlin.io.path.readText

fun config() = Json.decodeFromString<Config>(Path("config.json").readText(charset("utf-8")))

fun main() {
    val store = KeyStore.getInstance("pkcs12")
    val file = File(ClassLoader.getSystemResource("jombi.dev.p12").toURI())
    store.load(file.inputStream(), config().keyStorePassword.toCharArray())

    val env = applicationEngineEnvironment {
        envConfig()

        sslConnector(
            keyStore = store,
            keyAlias = "jombi.dev",
            keyStorePassword = { config().keyStorePassword.toCharArray() },
            privateKeyPassword = { config().keyStorePassword.toCharArray() }
        ) {
            port = 2096
            keyStorePath = file
        }
    }
    embeddedServer(Netty, env).start(wait = true)
}

fun ApplicationEngineEnvironmentBuilder.envConfig() {
    module {
        module()
    }
}

fun Application.module() {
    DatabaseFactory
    configureRouting()
}
