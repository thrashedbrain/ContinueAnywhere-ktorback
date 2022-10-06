package com.ContinueAnywhere.database.devices

@kotlinx.serialization.Serializable
data class DeviceDTO(
    val id: Int? = null,
    val active: Boolean,
    val fcmToken: String,
    val name: String,
    val userId: Int
)
