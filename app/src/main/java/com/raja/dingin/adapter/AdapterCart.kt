package com.raja.dingin.adapter

import android.content.Context
import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.denzcoskun.imageslider.models.SlideModel
import com.raja.dingin.R
import com.raja.dingin.model.res.ResCart
import kotlinx.android.synthetic.main.image_cart.view.*

class AdapterCart(var list: ArrayList<ResCart>) :
    RecyclerView.Adapter<AdapterCart.ViewHolder>() {

    var listener: RecyclerViewCartClickListener? = null
    var totalnominal = 0L
    var checkBoxStateArray = SparseBooleanArray()
    var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.image_cart, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int{
        return if (list == null){
            0
        } else list!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var qty = 0
        holder.checkbox.isChecked = checkBoxStateArray.get(position, false)

//        if (holder.checkbox.isChecked == true){
//            holder.itemView.btnIncrement.isEnabled = true
//            holder.itemView.btnDecrement.isEnabled = true
//        }
//        else if (holder.checkbox.isChecked == false){
//            holder.itemView.btnIncrement.isEnabled = false
//            holder.itemView.btnDecrement.isEnabled = false
//        }

        Glide.with(context!!.applicationContext)
            .load(list[position].image)
            .placeholder(R.drawable.logo)
            .error(R.drawable.logo)
            .into(holder.itemView.iv_foto)

        holder.itemView.tvProductNama.text = list[position].name_product
        holder.itemView.tvHarga.text =  "Price " + list[position].harga.toString()

        holder.itemView.tvDisplay.setText(list[position].qty.toString())
        qty = list[position].qty

        holder.itemView.btnIncrement.setOnClickListener {
            qty += 1
            if (qty > list[position].sisa_stok){
                qty -= 1
            }
            else{
                val totalprice = list[position].harga * qty

                holder.itemView.tvTotalPrice.text =  totalprice.toString()
                holder.itemView.tvDisplay.setText(qty.toString())
            }
        }

        holder.itemView.btnDecrement.setOnClickListener {
            qty -= 1
            if (qty < 1){
                qty += 1
            }
            else{
                val totalprice = list[position].harga * qty

                holder.itemView.tvTotalPrice.text =  totalprice.toString()
                holder.itemView.tvDisplay.setText(qty.toString())
            }

        }
        //holder.itemView.number_picker.max = list[position].sisa_stok
        val totalprice = list[position].harga * holder.itemView.tvDisplay.text.toString().toInt()
        holder.itemView.tvTotalPrice.text =  totalprice.toString()
        holder.itemView.tvStok.text =  "Stok " + list[position].sisa_stok.toString()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var checkbox: CheckBox = itemView.cbItem

        init {

            checkbox.setOnClickListener {
                if (!checkBoxStateArray.get(adapterPosition, false)) {
                    checkbox.isChecked = true
                    checkBoxStateArray.put(adapterPosition, true)
                    totalnominal += itemView.tvTotalPrice.text.toString().toInt()
                } else {
                    checkbox.isChecked = false
                    checkBoxStateArray.put(adapterPosition, false)
                    totalnominal -= itemView.tvTotalPrice.text.toString().toInt()
                }
                showTotal(totalnominal)
            }
        }
    }

    private fun showTotal(totalnominal: Long) {
        listener?.onItemClicked(totalnominal)
    }
}