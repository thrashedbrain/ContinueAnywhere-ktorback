package com.ContinueAnywhere.models.device

@kotlinx.serialization.Serializable
data class DeviceAddReceiveRemote(
    val active: Boolean,
    val fcmToken: String?,
    val name: String
)

@kotlinx.serialization.Serializable
data class DeviceUpdateReceiveRemote(
    val id: Int,
    val active: Boolean,
    val fcmToken: String?,
    val name: String
)

@kotlinx.serialization.Serializable
data class DevicesReceiveRemote(
    val email: String
)

@kotlinx.serialization.Serializable
data class DeviceAddResponseRemote(
    val device: DeviceResponse
)

@kotlinx.serialization.Serializable
data class DeviceUpdateResponseRemote(
    val device: DeviceResponse
)

@kotlinx.serialization.Serializable
data class DevicesResponseRemote(
    val devices: List<DeviceResponse>
)

@kotlinx.serialization.Serializable
data class DeviceResponse(
    val id: Int?,
    val active: Boolean,
    val fcmToken: String?,
    val name: String
)