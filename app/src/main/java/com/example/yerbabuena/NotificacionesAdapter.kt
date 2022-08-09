package com.example.yerbabuena

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NotificacionesAdapter : RecyclerView.Adapter<NotificacionesAdapter.ViewHolder>() {
    val pedidos = arrayOf(
        "Yerba buena",
        "Yerba buena",
        "Yerba buena"
    )
    val pagos = arrayOf(
        "Pago confirmado",
        "Pedido en camino",
        "Pedido entregado"
    )
    val mensajes = arrayOf(
        "Preparando Pedido",
        "Espere su pedido",
        "Disfrute de su pedido"
    )

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.list_item, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.textpedido.text = pedidos[i]
        viewHolder.textpago.text = pagos[i]
        viewHolder.textmensaje.text = mensajes[i]
    }

    override fun getItemCount(): Int {
        return pedidos.size

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var textpedido: TextView
        var textpago: TextView
        var textmensaje: TextView



        init {
            textpedido = itemView.findViewById(R.id.not_pedido)
            textpago = itemView.findViewById(R.id.not_pago)
            textmensaje = itemView.findViewById(R.id.not_mensaje)

        }
    }
}
