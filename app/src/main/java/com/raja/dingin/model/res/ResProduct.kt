package com.raja.dingin.model.res

data class ResProduct(
    val detailImage: List<DetailImage>,
    val harga: Int,
    val inv_id: Int,
    val kategori: String,
    var kode_product: String,
    val name_cabang: String,
    val name_product: String,
    val product_id: Int,
    val sisa_stok: Int,
    val stok_awal: Int
)