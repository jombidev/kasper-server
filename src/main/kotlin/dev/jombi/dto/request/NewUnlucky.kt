package dev.jombi.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class NewUnlucky(val firstName: String, val secondName: String)