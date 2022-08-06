package com.raja.dingin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.raja.dingin.R
import com.raja.dingin.databinding.ImageCategoriBinding
import com.raja.dingin.model.res.ResCategori

class AdapterCategori (
    private val resCategori: List<ResCategori>
) : RecyclerView.Adapter<AdapterCategori.NegaraViewHolder>() {

    var listener: RecyclerViewClickListener? = null
    private var viewBinding: ImageCategoriBinding? = null
    lateinit var mContext: Context

    inner class NegaraViewHolder(
        val itemCategoriBinding: ImageCategoriBinding
    ) : RecyclerView.ViewHolder(itemCategoriBinding.root)

    // untuk mendapatkan jumlah data yang dimasukkan ke dalam adapter
    override fun getItemCount() = resCategori.size

    // untuk membuat setiap item recyclerview berdasarkan jumlah data yang dimasukkan ke dalam adapter
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : NegaraViewHolder{
        mContext = parent.context
        viewBinding = ImageCategoriBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NegaraViewHolder(viewBinding!!)
    }


    // untuk memasukkan atau set data ke dalam view
    override fun onBindViewHolder(holder: NegaraViewHolder, position: Int) {
        holder.itemCategoriBinding.tvNameCategori.text = resCategori[position].sub_kategori
        Glide.with(mContext)
            .load(resCategori[position].image)
            .into(holder.itemCategoriBinding.ivCategori)

        // event onclick pada setiap item
        holder.itemCategoriBinding.lyCard.setOnClickListener {
            listener?.onItemClicked(it, resCategori[position])
        }
    }
}