package com.example.yerbabuena

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.yerbabuena.classes.Pedido
import com.example.yerbabuena.classes.Menu
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class PromocionesAdapter(options: FirebaseRecyclerOptions<Menu>) : FirebaseRecyclerAdapter<Menu, PromocionesAdapter.ViewHolder>(options) {
    private lateinit var view: ViewGroup

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

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Menu) {
        holder.textTitle.text = model.name
        holder.textDes.text = model.description
        holder.itemshare.setImageResource(R.drawable.share)
        holder.itemshopping.setImageResource(R.drawable.shoppingcart)
        holder.image.setImageResource(R.drawable.itemensalda)

        holder.itemshopping.setOnClickListener {
            val pedido = Pedido()
            pedido.name = model.name
            pedido.description = model.description
            pedido.price = model.price
            pedido.thumbnail = model.thumbnail

            val userid = Firebase.auth.currentUser!!.uid
            val ref = Firebase.database.getReference("Usuarios").child(userid)
            ref.child("Pedidos").push().setValue(pedido).addOnSuccessListener {
                var builder: AlertDialog.Builder = AlertDialog.Builder(view.context)
                builder.setTitle(R.string.app_name).setMessage(R.string.msg_order_added_success)
                builder.setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
                builder.create().show()
            }
        }
    }
}
