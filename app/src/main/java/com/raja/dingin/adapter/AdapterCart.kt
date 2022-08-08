package com.raja.dingin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raja.dingin.databinding.ImageProductBinding
import com.raja.dingin.model.res.ResCart

class AdapterCart (
    private val resCart: MutableList<ResCart> = mutableListOf()
) : RecyclerView.Adapter<AdapterCart.NegaraViewHolder>() {

    var listener: RecyclerViewCartClickListener? = null
    private var viewBinding: ImageProductBinding? = null
    lateinit var mContext: Context

    inner class NegaraViewHolder(
        val itemCategoriBinding: ImageProductBinding
    ) : RecyclerView.ViewHolder(itemCategoriBinding.root)

    // untuk mendapatkan jumlah data yang dimasukkan ke dalam adapter
    override fun getItemCount() = resCart.size

    // untuk membuat setiap item recyclerview berdasarkan jumlah data yang dimasukkan ke dalam adapter
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : NegaraViewHolder{
        mContext = parent.context
        viewBinding = ImageProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NegaraViewHolder(viewBinding!!)
    }

    fun setView(resCartNew: List<ResCart>) {
        resCart.clear() // clear list
        resCart.addAll(resCartNew)
        notifyDataSetChanged() // let your adapter know about the changes and reload view.
    }

    // untuk memasukkan atau set data ke dalam view
    override fun onBindViewHolder(holder: NegaraViewHolder, position: Int) {
        holder.itemCategoriBinding.tvProductNama.text = resCart[position].name_product
//        Glide.with(mContext)
//            .load(resCart[position].detailImage[0].image)
//            .placeholder(R.drawable.logo)
//            .error(R.drawable.logo)
//            .into(holder.itemCategoriBinding.ivFoto)

        holder.itemCategoriBinding.tvHarga.text =  "Price " + resCart[position].harga.toString()
        holder.itemCategoriBinding.tvStok.text =  "Sub Total " + resCart[position].sub_total.toString()

        // event onclick pada setiap item
        holder.itemCategoriBinding.lyCard.setOnClickListener {
            listener?.onItemClicked(it, resCart[position])
        }
    }
}