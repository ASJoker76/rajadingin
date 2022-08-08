package com.raja.dingin.model.req

data class ReqCart(
    val harga: Int,
    val inv_id: Int,
    val name_product: String,
    val qty: Int,
    val sub_total: Int
)