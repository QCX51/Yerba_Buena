package com.example.yerbabuena

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.widget.CheckBox

class PedidosListAdapter : RecyclerView.Adapter<PedidosListAdapter.ViewHolder>() {

    val pedidoslist = arrayOf(
        "Pedido 1",
        "Pedido 2",
        "Pedido 3",
        "Pedido 4",
        "Pedido 5",
        "Pedido 6",
        "Pedido 7",
        "Pedido 8",


    )


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.card_pedidos_list, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.mycheck.isChecked=false
        viewHolder.price.text=pedidoslist[i]
    }

    override fun getItemCount(): Int {
        return pedidoslist.size

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mycheck:CheckBox
        var price:TextView


        init {
            mycheck = itemView.findViewById(R.id.CheckboxList)
            price=itemView.findViewById(R.id.item_pedido)
        }
    }
}
