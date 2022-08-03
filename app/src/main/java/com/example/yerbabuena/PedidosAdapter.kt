package com.example.yerbabuena

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.widget.CheckBox

class PedidosAdapter : RecyclerView.Adapter<PedidosAdapter.ViewHolder>() {
    val titlescook = arrayOf(
        "Comida",
        "Ensalada",
        "Postre"
    )
    val detalle = arrayOf(
        "Rica ensalada",
        "Rica ensalada",
        "Rica ensalada"
    )
    val imagenes = arrayOf(
        R.drawable.itemensalda,
        R.drawable.itemensalda,
        R.drawable.itemensalda
    )
    val edits = arrayOf(
        R.drawable.edit_icon,
        R.drawable.edit_icon,
        R.drawable.edit_icon
    )
    val deletes = arrayOf(
        R.drawable.delete_icon,
        R.drawable.delete_icon,
        R.drawable.delete_icon
    )

    val prices = arrayOf(
        "$400.00",
        "$120.00",
        "$390.00"
    )


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.card_pedidos_model, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.titleCook.text = titlescook[i]
        viewHolder.detail.text = detalle[i]
        viewHolder.itemedit.setImageResource(edits[i])
        viewHolder.itemdelete.setImageResource(deletes[i])
        viewHolder.imagen.setImageResource(imagenes[i])
        viewHolder.mycheck.isChecked=false
        viewHolder.price.text=prices[i]
    }

    override fun getItemCount(): Int {
        return titlescook.size

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imagen: ImageView
        var titleCook: TextView
        var detail: TextView
        var itemdelete: ImageView
        var itemedit: ImageView
        var mycheck:CheckBox
        var price:TextView


        init {
            mycheck = itemView.findViewById(R.id.myCheckbox)
            itemedit = itemView.findViewById(R.id.edit)
            imagen = itemView.findViewById(R.id.imagepedido)
            titleCook = itemView.findViewById(R.id.title_cook)
            detail = itemView.findViewById(R.id.Detail)
            itemdelete = itemView.findViewById(R.id.delete)
            price=itemView.findViewById(R.id.item_price)
        }
    }
}
