package com.raja.dingin.adapter

import android.view.View
import com.raja.dingin.model.res.ResCart
import com.raja.dingin.model.res.ResCategori
import com.raja.dingin.model.res.ResProduct

interface RecyclerViewCartClickListener {

    // method yang akan dipanggil di MainActivity
    fun onItemClicked(view: View, resCart: ResCart)

}