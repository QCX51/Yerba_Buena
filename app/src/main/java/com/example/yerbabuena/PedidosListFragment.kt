package com.example.yerbabuena

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class PedidosListFragment : Fragment() {
    private var layoutManager: RecyclerView.LayoutManager? =null
    private var adapter: RecyclerView.Adapter<PedidosListAdapter.ViewHolder>? =null
    val recyclerView = view?.findViewById<RecyclerView>(R.id.recyclerView)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_pedidos_list, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerpedidoslist)


        adapter = PedidosListAdapter()

        /*val adapter = object : FirebaseRecyclerAdapter<Menus, ChatHolder>(options) {}*/
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.adapter = adapter

        return view
    }
}