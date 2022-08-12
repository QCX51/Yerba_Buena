package com.example.yerbabuena

import android.app.AlertDialog
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.yerbabuena.classes.Pedido
import com.example.yerbabuena.menus.Promociones
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class PromocionesAdapter(options: FirebaseRecyclerOptions<Promociones>) : FirebaseRecyclerAdapter<Promociones, PromocionesAdapter.ViewHolder>(options) {
    private lateinit var view: ViewGroup;

    val titles = arrayOf(
        "Comida",
        "Ensalada",
        "Postre"
    )
    val details = arrayOf(
        "Rica ensalada",
        "Rica ensalada",
        "Rica ensalada"
    )
    val images = arrayOf(
        R.drawable.itemensalda,
        R.drawable.itemensalda,
        R.drawable.itemensalda
    )
    val shares = arrayOf(
        R.drawable.share,
        R.drawable.share,
        R.drawable.share
    )
    val shopings = arrayOf(
        R.drawable.shoppingcart,
        R.drawable.shoppingcart,
        R.drawable.shoppingcart

    )
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        view = viewGroup
        val v =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.card_promociones, viewGroup, false)
        return ViewHolder(v)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView
        var textTitle: TextView
        var textDes: TextView
        var itemshopping: ImageView
        var itemshare: ImageView


        init {
            itemshare = itemView.findViewById(R.id.share)
            image = itemView.findViewById(R.id.itemImage)
            textTitle = itemView.findViewById(R.id.itemTitle)
            textDes = itemView.findViewById(R.id.itemDetail)
            itemshopping = itemView.findViewById(R.id.shopping)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Promociones) {
        holder.textTitle.text = model.getName()
        holder.textDes.text = model.getDescription()
        holder.itemshare.setImageResource(R.drawable.share)
        holder.itemshopping.setImageResource(R.drawable.shoppingcart)
        holder.image.setImageResource(R.drawable.itemensalda)

        holder.itemshopping.setOnClickListener {
            val pedido = Pedido()
            pedido.name = model.getName()
            pedido.description = model.getDescription()
            pedido.price = model.getPrice()
            //pedido.imageuri = model.getImageUri()

        }
    }
}
