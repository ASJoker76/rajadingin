package com.raja.dingin.model.req

data class ReqRegister(
    val address: String,
    val email: String,
    val kabupaten: String,
    val kecamatan: String,
    val kelurahan: String,
    val latitude: String,
    val longitude: String,
    val nameUser: String,
    val password: String,
    val phone: String,
    val postal_code: String,
    val provinsi: String,
    val username: String
)