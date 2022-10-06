package com.ContinueAnywhere

import com.ContinueAnywhere.database.devices.Devices
import com.ContinueAnywhere.database.users.UserDao
import com.ContinueAnywhere.database.users.Users
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.ContinueAnywhere.plugins.*
import com.ContinueAnywhere.router.acountApi
import com.ContinueAnywhere.router.authApi
import com.ContinueAnywhere.utils.TokenManager
import com.ContinueAnywhere.utils.gson1
import com.typesafe.config.ConfigFactory
import io.ktor.serialization.gson.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.config.*
import io.ktor.server.plugins.contentnegotiation.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {
    val db = Database.connect("jdbc:sqlite:/data/data.db", "org.sqlite.JDBC")

    transaction(db) {
        SchemaUtils.create(
            Users,
            Devices
        )
    }

    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        install(ContentNegotiation) {
            gson1()
        }
        module()
        configureRouting()
        authApi()
        acountApi()

    }.start(wait = true)
}

fun Application.module() {
    val config = HoconApplicationConfig(ConfigFactory.load())
    val tokenManager = TokenManager(config)
    install(Authentication) {

        val userDao = UserDao()

        jwt("auth-jwt") {
            verifier(tokenManager.verifyToken())
            realm = config.property("realm").getString()
            validate { jwtCredential ->
                val login = jwtCredential.payload.getClaim("email").asString()
                if (userDao.findUser(login) != null) {
                    JWTPrincipal(payload = jwtCredential.payload)
                } else {
                    null
                }
            }
        }
    }
}
