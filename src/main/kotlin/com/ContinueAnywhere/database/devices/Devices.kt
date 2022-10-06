package com.ContinueAnywhere.database.devices

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Devices : IntIdTable() {
    val name = Devices.varchar("name", 25)
    val fcmToken = Devices.varchar("fcm_token", 100)
    val active = Devices.bool("active")
    val userId = Devices.integer("user_id")

    class DeviceRow(id: EntityID<Int>): IntEntity(id) {
        companion object: IntEntityClass<DeviceRow>(Devices)
        var name by Devices.name
        var fcmToken by Devices.fcmToken
        var active by Devices.active
        var userId by Devices.userId

        fun toDeviceDTO() = DeviceDTO(id.value, active, fcmToken, name, userId)
    }
}