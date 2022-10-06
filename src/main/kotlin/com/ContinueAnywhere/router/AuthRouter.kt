package com.ContinueAnywhere.router

import com.ContinueAnywhere.controllers.AuthController
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.authApi() {

    routing {

        route("/auth") {

            post("/signin") {
                val authController = AuthController(call)
                authController.performSignin()
            }

            post("/signup") {
                val authController = AuthController(call)
                authController.performSignUp()
            }
        }
    }
}