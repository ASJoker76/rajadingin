package com.raja.dingin.model.req

data class ReqCart(
    val harga: Int,
    val image: String,
    val inv_id: Int,
    val name_product: String,
    val qty: Int,
    val sisa_stok: Int,
    val stok_awal: Int,
    val sub_total: Int
)