package dev.jombi

import kotlinx.serialization.Serializable

@Serializable
data class Config(val keyStorePassword: String)