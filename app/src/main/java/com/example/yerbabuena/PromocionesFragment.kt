package com.example.yerbabuena

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.yerbabuena.menus.Menus
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class PromocionesFragment : Fragment() {
    private var layoutManager: RecyclerView.LayoutManager? =null
    private var adapter: RecyclerView.Adapter<PromocionesAdapter.ViewHolder>? =null
    val recyclerView = view?.findViewById<RecyclerView>(R.id.recyclerView)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_promociones, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclermain)


        adapter = PromocionesAdapter()

        /*val adapter = object : FirebaseRecyclerAdapter<Menus, ChatHolder>(options) {}*/
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.adapter = adapter

        return view
    }
}