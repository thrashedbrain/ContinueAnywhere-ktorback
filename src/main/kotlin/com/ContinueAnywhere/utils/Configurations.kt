package com.ContinueAnywhere.utils

import com.google.gson.GsonBuilder
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.serialization.gson.*

public fun Configuration.gson1(
    contentType: ContentType = ContentType.Application.Json,
    block: GsonBuilder.() -> Unit = {}
) {
    val builder = GsonBuilder()
    builder.apply(block)
    val converter = GsonConverter(builder.create())
    register(contentType, converter)
}