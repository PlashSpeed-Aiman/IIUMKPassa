package org.forthify.passxplat.model

import kotlinx.serialization.Serializable

@Serializable
data class StudentCredentials(
    val matricNumber : String,
    val password : String
)
