package com.example.yerbabuena

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.yerbabuena.classes.Menu
import com.example.yerbabuena.classes.Pedido
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MenuAdapter(options: FirebaseRecyclerOptions<Menu>) : FirebaseRecyclerAdapter<Menu, MenuAdapter.ViewHolder>(options) {

    private lateinit var view: ViewGroup

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemShare: ImageView
        var itemShopping: ImageView
        var itemImage: ImageView
        var itemTitle: TextView
        var itemDetail: TextView
        var itemCount: TextView

        init {
            itemShare = itemView.findViewById(R.id.share)
            itemImage = itemView.findViewById(R.id.item_image)
            itemTitle = itemView.findViewById(R.id.item_title)
            itemDetail = itemView.findViewById(R.id.item_detail)
            itemCount = itemView.findViewById(R.id.item_count)
            itemShopping = itemView.findViewById(R.id.shopping)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Menu) {
        if (position <= holder.bindingAdapter?.itemCount!!) {
            holder.itemTitle.text = model.name
            holder.itemDetail.text = model.description
            holder.itemCount.text = "$${model.price}"
            holder.itemShare.setImageResource(R.drawable.share)
            holder.itemShopping.setImageResource(R.drawable.shoppingcart)
            holder.itemImage.setImageResource(R.drawable.itemensalda)

            holder.itemShopping.setOnClickListener {
                val pedido = Pedido()
                pedido.name = model.name
                pedido.description = model.description
                pedido.price = model.price
                pedido.thumbnail = model.thumbnail

                val userid = Firebase.auth.currentUser?.uid
                if (userid != null)
                {
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
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.card_layout, parent, false))
        view = parent
        return v
    }
}