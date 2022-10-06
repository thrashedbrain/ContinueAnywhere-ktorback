package com.ContinueAnywhere.utils

import com.ContinueAnywhere.database.users.UserDTO
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.config.*
import java.util.*

class TokenManager(config: HoconApplicationConfig) {
    private val audience = config.property("audience").getString()
    private val secret = config.property("secret").getString()
    private val issuer = config.property("issuer").getString()
    val expirationDate = System.currentTimeMillis() + 6000000


    fun generateToken(user: UserDTO): String {
        return JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("email", user.email)
            .withExpiresAt(Date(expirationDate))
            .sign(Algorithm.HMAC256(secret))
    }

    fun verifyToken(): JWTVerifier {
        return JWT
            .require(Algorithm.HMAC256(secret))
            .withAudience(audience)
            .withIssuer(issuer)
            .build()
    }
}