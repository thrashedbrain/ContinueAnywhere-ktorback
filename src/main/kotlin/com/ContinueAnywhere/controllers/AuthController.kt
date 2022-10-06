package com.ContinueAnywhere.controllers

import com.ContinueAnywhere.database.users.UserDTO
import com.ContinueAnywhere.database.users.UserDao
import com.ContinueAnywhere.database.users.Users
import com.ContinueAnywhere.models.base.BaseResponse
import com.ContinueAnywhere.models.user.SignInReceiveRemote
import com.ContinueAnywhere.models.user.SignInResponseRemote
import com.ContinueAnywhere.utils.TokenManager
import com.typesafe.config.ConfigFactory
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.config.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import org.jetbrains.exposed.exceptions.ExposedSQLException

class AuthController(private val call: ApplicationCall) {

    val tokenManager = TokenManager(HoconApplicationConfig(ConfigFactory.load()))
    val userDao = UserDao()

    suspend fun performSignin() {
        val receive = call.receive<SignInReceiveRemote>()
        val userDTO = userDao.findUser(receive.email)

        //TODO CHECK PASS AND EMAIL NOT EMPTY
        if (userDTO == null) {
            call.respond(HttpStatusCode.BadRequest, BaseResponse().getBaseResponse("null", "User not found"))
        } else {
            if (userDTO.password == receive.password) {
                val token = tokenManager.generateToken(userDTO)

                call.respond(BaseResponse().getBaseResponse(SignInResponseRemote(token), ""))
            } else {
                call.respond(HttpStatusCode.BadRequest, BaseResponse().getBaseResponse("null", "Invalid password"))
            }
        }
    }

    suspend fun performSignUp() {
        val receive = call.receive<SignInReceiveRemote>()

        //TODO CHECK VALIDATION
        print("asdasd ${receive}")

        val userDTO = userDao.findUser(receive.email)
        if (userDTO != null) {
            call.respond(HttpStatusCode.BadRequest, BaseResponse().getBaseResponse("null", "User exists"))
        } else {

            val user = UserDTO(
                password = receive.password,
                email = receive.email
            )

            try {
                userDao.insertUser(user)
            } catch (e: ExposedSQLException) {
                call.respond(HttpStatusCode.Conflict, BaseResponse().getBaseResponse("null", "User already exists"))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, BaseResponse().getBaseResponse("null","Can't create user ${e.localizedMessage}"))
            }

            val token = tokenManager.generateToken(user)
            call.respond(BaseResponse().getBaseResponse(SignInResponseRemote(token), ""))
        }
    }
}