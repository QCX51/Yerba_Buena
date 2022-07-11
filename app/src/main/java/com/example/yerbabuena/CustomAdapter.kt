package com.example.yerbabuena
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustomAdapter: RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

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
    val counts = arrayOf(
        "$242.00","$230.00","$450.00"
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
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_layout, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.itemTitle.text=titles[i]
        viewHolder.itemDetail.text = details[i]
        viewHolder.itemShare.setImageResource(shares[i])
        viewHolder.itemShopping.setImageResource(shopings[i])
        viewHolder.itemImage.setImageResource(images[i])
        counts[i].toString().also { viewHolder.itemCount.text = it }


    }

    override fun getItemCount(): Int {
        return titles.size

    }

    inner class ViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){
        var itemShare:ImageView
        var itemShopping:ImageView
        var itemImage:ImageView
        var itemTitle:TextView
        var itemDetail:TextView
        var itemCount:TextView

        init {
            itemShare = itemView.findViewById(R.id.share)
            itemImage = itemView.findViewById(R.id.item_image)
            itemTitle = itemView.findViewById(R.id.item_title)
            itemDetail = itemView.findViewById(R.id.item_detail)
            itemCount = itemView.findViewById(R.id.item_count)
            itemShopping = itemView.findViewById(R.id.shopping)
        }
    }
}
