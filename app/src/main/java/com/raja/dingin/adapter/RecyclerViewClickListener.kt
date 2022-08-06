package com.raja.dingin.adapter

import android.view.View
import com.raja.dingin.model.res.ResCategori
import com.raja.dingin.model.res.ResProduct

interface RecyclerViewClickListener {

    // method yang akan dipanggil di MainActivity
    fun onItemClicked(view: View, resCategori: ResCategori)

    fun onItemClicked(view: View, resProduct: ResProduct)
}