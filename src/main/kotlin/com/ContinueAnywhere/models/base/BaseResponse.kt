package com.ContinueAnywhere.models.base

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.put

class BaseResponse {

    inline fun <reified T> getBaseResponse(data: T?, error: String): String {
        return buildJsonObject {
            val json = Json { encodeDefaults = true }
            put("data", json.encodeToJsonElement(data))
            put("error", error)
        }.toString()
    }
}
