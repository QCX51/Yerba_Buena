package com.example.yerbabuena
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.yerbabuena.classes.Menu
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class ProductosAdapter(options: FirebaseRecyclerOptions<Menu>) : FirebaseRecyclerAdapter<Menu, ProductosAdapter.ViewHolder>(options) {


    private lateinit var view: ViewGroup
    override fun onDataChanged() {
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var thumbnail: ImageView
        var delete: ImageView
        var price: TextView

        init {
            thumbnail = itemView.findViewById(R.id.menu_thumbnail)
            delete = itemView.findViewById(R.id.delete_menu)
            price = itemView.findViewById(R.id.menu_price)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Menu) {
        holder.thumbnail.setImageResource(R.drawable.itemensalda)
        holder.price.text = "$${model.price}"
        holder.thumbnail.setImageResource(R.drawable.itemensalda)
    //holder.view.visibility = View.GONE
    // holder.view.layoutParams.height = 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.card_products, parent, false))
        view = parent
        return v
    }
}