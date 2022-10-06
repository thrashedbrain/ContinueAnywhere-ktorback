package com.ContinueAnywhere.database.devices

import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class DeviceDao {

    suspend fun addDevice(deviceDTO: DeviceDTO) = newSuspendedTransaction {
        val id = Devices.insertAndGetId {
            it[name] = deviceDTO.name
            it[fcmToken] = deviceDTO.fcmToken
            it[active] = deviceDTO.active
            it[userId] = deviceDTO.userId
        }
        return@newSuspendedTransaction Devices.DeviceRow.findById(id)?.toDeviceDTO()
    }

    suspend fun getDevice(id: Int) = newSuspendedTransaction {
        return@newSuspendedTransaction Devices.DeviceRow.findById(id)?.toDeviceDTO()
    }

    suspend fun getDevices(userId: Int) = newSuspendedTransaction {
        val devices = Devices.DeviceRow.find { Devices.userId eq userId }.map { it.toDeviceDTO() }
        return@newSuspendedTransaction devices
    }
}