package com.raja.dingin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.raja.dingin.R
import com.raja.dingin.databinding.ImageProductBinding
import com.raja.dingin.model.res.ResProduct

class AdapterProduct (
    private val resProduct: MutableList<ResProduct> = mutableListOf()
) : RecyclerView.Adapter<AdapterProduct.NegaraViewHolder>() {

    var listener: RecyclerViewHomeClickListener? = null
    private var viewBinding: ImageProductBinding? = null
    lateinit var mContext: Context

    inner class NegaraViewHolder(
        val itemCategoriBinding: ImageProductBinding
    ) : RecyclerView.ViewHolder(itemCategoriBinding.root)

    // untuk mendapatkan jumlah data yang dimasukkan ke dalam adapter
    override fun getItemCount() = resProduct.size

    // untuk membuat setiap item recyclerview berdasarkan jumlah data yang dimasukkan ke dalam adapter
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : NegaraViewHolder{
        mContext = parent.context
        viewBinding = ImageProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NegaraViewHolder(viewBinding!!)
    }

    fun setView(resProductNew: List<ResProduct>) {
        resProduct.clear() // clear list
        resProduct.addAll(resProductNew)
        notifyDataSetChanged() // let your adapter know about the changes and reload view.
    }

    // untuk memasukkan atau set data ke dalam view
    override fun onBindViewHolder(holder: NegaraViewHolder, position: Int) {
        holder.itemCategoriBinding.tvProductNama.text = resProduct[position].name_product
        Glide.with(mContext)
            .load(resProduct[position].detailImage[0].image)
            .placeholder(R.drawable.logo)
            .error(R.drawable.logo)
            .into(holder.itemCategoriBinding.ivFoto)

        holder.itemCategoriBinding.tvHarga.text =  "Price " + resProduct[position].harga.toString()
        holder.itemCategoriBinding.tvStok.text =  "Stock " + resProduct[position].sisa_stok.toString()

        // event onclick pada setiap item
        holder.itemCategoriBinding.lyCard.setOnClickListener {
            listener?.onItemClicked(it, resProduct[position])
        }
    }
}