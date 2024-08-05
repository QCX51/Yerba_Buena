package com.example.yerbabuena

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PedidosListFragment : Fragment() {
    private var adapter: RecyclerView.Adapter<PedidosListAdapter.ViewHolder>? =null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_pedidos_list, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rcv_pedidos_list)
        adapter = PedidosListAdapter()

        val layout = object : LinearLayoutManager(view.context) {
            override fun onLayoutChildren(
                recycler: RecyclerView.Recycler?,
                state: RecyclerView.State?
            ) {
                try {
                    super.onLayoutChildren(recycler, state)
                }catch (e:IndexOutOfBoundsException) {
                    Log.println(Log.ERROR, null, e.message!!)
                }
            }
        }

        recyclerView.layoutManager = layout
        recyclerView.adapter = adapter
        return view
    }
}