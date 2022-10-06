package com.ContinueAnywhere.router

import com.ContinueAnywhere.controllers.AccountController
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Application.acountApi() {

    routing {
        route("/account") {

            authenticate("auth-jwt") {

                post("/add_device") {
                    val deviceController = AccountController(call)
                    deviceController.addDevice()
                }

                post("/select_device") {
                    //TODO SELECT DEVICE
                }

                get("/devices") {
                    val deviceController = AccountController(call)
                    deviceController.getDevices()
                }
            }
        }
    }
}