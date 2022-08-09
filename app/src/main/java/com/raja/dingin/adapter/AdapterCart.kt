package com.raja.dingin.adapter

import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.raja.dingin.R
import com.raja.dingin.model.res.ResCart
import kotlinx.android.synthetic.main.image_cart.view.*

class AdapterCart(var list: ArrayList<ResCart>) :
    RecyclerView.Adapter<AdapterCart.ViewHolder>() {

    var listener: RecyclerViewCartClickListener? = null
    var totalnominal = 0L
    var checkBoxStateArray = SparseBooleanArray()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val context = parent.context
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

        holder.checkbox.isChecked = checkBoxStateArray.get(position, false)

        holder.itemView.tvProductNama.text = list[position].name_product

        holder.itemView.tvHarga.text =  "Price " + list[position].harga.toString()
        holder.itemView.tvStok.text =  "Sub Total " + list[position].sub_total.toString()

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var checkbox: CheckBox = itemView.cbItem

        init {

            checkbox.setOnClickListener {
                if (!checkBoxStateArray.get(adapterPosition, false)) {
                    checkbox.isChecked = true
                    checkBoxStateArray.put(adapterPosition, true)
                    totalnominal += list[position].sub_total
                } else {
                    checkbox.isChecked = false
                    checkBoxStateArray.put(adapterPosition, false)
                    totalnominal -= list[position].sub_total
                }
                showTotal(totalnominal)
            }
        }
    }

    private fun showTotal(totalnominal: Long) {
        listener?.onItemClicked(totalnominal)
    }
}