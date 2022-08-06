package com.raja.dingin.model.res

data class ResLogin(
    val status: Int,
    val username: String,
    val role: String,
    val token: String
)