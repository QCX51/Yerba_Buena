package com.example.yerbabuena

import android.app.Application
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.widget.CheckBox
import android.widget.Toast
import com.example.yerbabuena.classes.Pedido
import com.example.yerbabuena.menus.Menus
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class PedidosAdapter(options: FirebaseRecyclerOptions<Pedido>) : FirebaseRecyclerAdapter<Pedido, PedidosAdapter.ViewHolder>(options) {

    private lateinit var view: ViewGroup
    private lateinit var inflater: View
    private var total: Double = 0.0

    public fun getTotal():Double
    {
        return total
    }

    override fun onDataChanged() {
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.card_pedidos_model, viewGroup, false)
        view = viewGroup
        inflater = LayoutInflater.from(viewGroup.context).inflate(R.layout.fragment_pedidos, viewGroup, false)
        return ViewHolder(v)
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
            price = itemView.findViewById(R.id.item_price)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Pedido) {
        holder.titleCook.text = model.name
        holder.detail.text = model.description
        holder.itemedit.setImageResource(R.drawable.edit_icon)
        holder.itemdelete.setImageResource(R.drawable.delete_icon)
        holder.imagen.setImageResource(R.drawable.itemensalda)
        holder.mycheck.isChecked = false
        holder.price.text = "$${model.price}"

        //var totalTxt:TextView = inflater.findViewById(R.id.TotalPrice)
        total += model.price!!
        //totalTxt.text = "rrrr"

        holder.itemdelete.setOnClickListener {
            val key = this.getRef(position).key
            val userid = Firebase.auth.currentUser?.uid
            if (userid != null && key != null)
            {
                val ref = Firebase.database.getReference("Usuarios")
                    .child(userid).child("Pedidos").child(key).removeValue()
                    .addOnCompleteListener {
                        Toast.makeText(view.context, "Pedido eliminado", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}
