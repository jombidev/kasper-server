package dev.jombi.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class FailedResponse(val message: String)