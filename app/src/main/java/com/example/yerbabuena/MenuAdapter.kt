package com.example.yerbabuena

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.yerbabuena.menus.Menus
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class MenuAdapter(options: FirebaseRecyclerOptions<Menus>) : FirebaseRecyclerAdapter<Menus, MenuAdapter.ViewHolder>(options) {

    private lateinit var view: ViewGroup;
    override fun onDataChanged() {
    }

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

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Menus) {
        holder.itemTitle.text = model.getName()
        holder.itemDetail.text = model.getDescription()
        holder.itemCount.text = "$${model.getPrice()}"
        holder.itemShare.setImageResource(R.drawable.share)
        holder.itemShopping.setImageResource(R.drawable.shoppingcart)
        holder.itemImage.setImageResource(R.drawable.itemensalda)

        holder.itemShopping.setOnClickListener{
            var builder: AlertDialog.Builder = AlertDialog.Builder(view.context)
            builder.setTitle(R.string.app_name).setMessage("des: ${holder.itemDetail.text}")
            builder.setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            builder.create().show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.card_layout, parent, false))
        view = parent
        return v
    }
}