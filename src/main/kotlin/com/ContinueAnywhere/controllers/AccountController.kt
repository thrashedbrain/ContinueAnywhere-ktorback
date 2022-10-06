package com.ContinueAnywhere.controllers

import com.ContinueAnywhere.database.devices.DeviceDTO
import com.ContinueAnywhere.database.devices.DeviceDao
import com.ContinueAnywhere.database.users.UserDao
import com.ContinueAnywhere.models.base.BaseResponse
import com.ContinueAnywhere.models.device.*
import com.ContinueAnywhere.utils.TokenManager
import com.typesafe.config.ConfigFactory
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.config.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.jetbrains.exposed.exceptions.ExposedSQLException

class AccountController(private val call: ApplicationCall) {

    val tokenManager = TokenManager(HoconApplicationConfig(ConfigFactory.load()))
    val deviceDao = DeviceDao()
    val userDao = UserDao()

    suspend fun addDevice() {
        val receive = call.receive<DeviceAddReceiveRemote>()

        var deviceDTO: DeviceDTO? = null

        val principal = call.principal<JWTPrincipal>()
        val email = principal?.payload?.getClaim("email")?.asString() ?: ""
        val userId = userDao.getUserId(email)

        if (!email.isNullOrBlank()) {
            if (receive.name.isNullOrBlank()) {
                call.respond(HttpStatusCode.Conflict, BaseResponse().getBaseResponse("null", "No name"))
            } else {
                val devicesList = deviceDao.getDevices(userId) //TODO ADD ID
                if (devicesList.isEmpty() || devicesList.find { it.name == receive.name && it.fcmToken == receive.fcmToken } == null) {
                    val device = DeviceDTO(
                        active = receive.active,
                        fcmToken = receive.fcmToken ?: "",
                        name = receive.name,
                        userId = userId
                    )

                    try {
                        deviceDTO = deviceDao.addDevice(device)
                    } catch (e: ExposedSQLException) {
                        call.respond(HttpStatusCode.Conflict, BaseResponse().getBaseResponse("null", "Device already exists"))
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.BadRequest, BaseResponse().getBaseResponse("null", "Can't create device ${e.localizedMessage}"))
                    }
                    call.respond(
                        BaseResponse().getBaseResponse(
                            DeviceAddResponseRemote(
                                DeviceResponse(
                                    id = deviceDTO?.id ?: -1,
                                    active = device.active,
                                    fcmToken = device.fcmToken,
                                    name = device.name
                                )
                            ),
                            ""
                        )
                    )
                } else {
                    call.respond(HttpStatusCode.Conflict, BaseResponse().getBaseResponse("null", "Device already exists"))
                }
            }
        } else {
            call.respond(HttpStatusCode.Unauthorized, BaseResponse().getBaseResponse("null", "Auth err"))
        }
    }

    suspend fun getDevices() {
        val receive = call.receive<DevicesReceiveRemote>()

        val principal = call.principal<JWTPrincipal>()
        val email = principal?.payload?.getClaim("email")?.asString() ?: ""
        val userId = userDao.getUserId(email)

        if (!email.isNullOrBlank()) {
            val devices = deviceDao.getDevices(userId)
            call.respond(BaseResponse().getBaseResponse(devices, ""))
        } else {
            call.respond(HttpStatusCode.Unauthorized, BaseResponse().getBaseResponse("null", "auth err"))
        }
    }
}