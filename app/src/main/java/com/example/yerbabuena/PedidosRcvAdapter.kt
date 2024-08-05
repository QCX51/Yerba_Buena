package com.example.yerbabuena

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.widget.CheckBox
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.example.yerbabuena.classes.Pedido
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class PedidosRcvAdapter(options: FirebaseRecyclerOptions<Pedido>, activity: FragmentActivity) : FirebaseRecyclerAdapter<Pedido, PedidosRcvAdapter.ViewHolder>(options) {

    private lateinit var view: ViewGroup
    private var amount: Double = 0.0
    private var activity: FragmentActivity
    private var fragment: PedidosFragment

    init {
        this.activity = activity
        fragment = activity.supportFragmentManager.findFragmentByTag("PedidosFragment") as PedidosFragment
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

    override fun onDataChanged() {
        amount = 0.0
        val fragment = activity?.supportFragmentManager
            ?.findFragmentByTag("PedidosFragment") as PedidosFragment
        for (pedido:Pedido in this.snapshots)
        {
            amount += pedido.price!!
            fragment.updateAmount(amount)
        }
        super.onDataChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.card_pedidos, viewGroup, false)
        view = viewGroup
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Pedido) {
        holder.titleCook.text = model.name
        holder.detail.text = model.description
        holder.itemedit.setImageResource(R.drawable.edit_icon)
        holder.itemdelete.setImageResource(R.drawable.delete_icon)
        holder.imagen.setImageResource(R.drawable.itemensalda)
        holder.mycheck.isChecked = false
        holder.price.text = view.resources.getString(R.string.Price, model.price)

        holder.itemdelete.tag = this.getRef(position).key
        holder.itemdelete.setOnClickListener {
            val key = it.tag as String
            deleteItem(key)
        }
    }

    fun deleteItem(id:String) {
        Firebase.database.getReference("Usuarios")
            .child(Firebase.auth.currentUser?.uid!!).child("Pedidos").child(id).removeValue()
            .addOnCompleteListener {
                Toast.makeText(view.context, "Pedido eliminado", Toast.LENGTH_SHORT).show()
            }
    }

    fun recoverItem(id:String, pedido: Pedido) {
        Firebase.database.getReference("Usuarios")
            .child(Firebase.auth.currentUser?.uid!!).child("Pedidos").child(id).setValue(pedido)
            .addOnSuccessListener {
                Toast.makeText(view.context, activity.getText(R.string.recovered_item), Toast.LENGTH_SHORT).show()
            }
    }
}
