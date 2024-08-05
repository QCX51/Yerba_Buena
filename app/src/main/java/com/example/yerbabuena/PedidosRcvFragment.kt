package com.example.yerbabuena

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.yerbabuena.classes.Pedido
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

// the fragment initialization
class PedidosRcvFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_pedidos_rcv, container, false)
        recyclerView = view.findViewById(R.id.rcv_pedidos)

        val options = FirebaseRecyclerOptions.Builder<Pedido>()
        val userid = Firebase.auth.currentUser?.uid
        val ref = Firebase.database.getReference("Usuarios").child(userid!!).child("Pedidos")
        options.setQuery(ref, Pedido::class.java)
        options.setLifecycleOwner(this)

        val adapter = PedidosRcvAdapter(options.build(), requireActivity())

        val layout = object : LinearLayoutManager(view.context) {
            override fun onLayoutChildren(
                recycler: RecyclerView.Recycler?,
                state: RecyclerView.State?
            ) {
                try {
                    super.onLayoutChildren(recycler, state)
                }
                catch (e: java.lang.IndexOutOfBoundsException) {
                    Log.println(Log.ERROR, "", e.message!!)
                }
            }
        }

        var frameLayout: FrameLayout = view.findViewById(R.id.coordinatorLayout)
        val callback = object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                val pedido: Pedido = adapter.getItem(position)
                val key = adapter.getRef(position).key.toString()
                adapter.deleteItem(key)

                Snackbar.make(frameLayout, resources.getText(R.string.deleted_item), Snackbar.LENGTH_LONG)
                    .setAction(resources.getText(R.string.undo)) {
                        adapter.recoverItem(key, pedido)
                        recyclerView.smoothScrollToPosition(position)
                    }.show()
                super.onSwiped(viewHolder, direction)
            }
        }
        ItemTouchHelper(callback).attachToRecyclerView(recyclerView)
        recyclerView.layoutManager = layout
        recyclerView.adapter = adapter
        return view
    }
}