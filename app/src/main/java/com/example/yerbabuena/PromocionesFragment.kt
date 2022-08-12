package com.example.yerbabuena

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.yerbabuena.classes.Pedido
import com.example.yerbabuena.menus.Menus
import com.example.yerbabuena.menus.Promociones
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class PromocionesFragment : Fragment() {

    private lateinit var adapter: PromocionesAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_promociones, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclermain)


        val options = FirebaseRecyclerOptions.Builder<Promociones>()
        val userid = Firebase.auth.currentUser?.uid
        if (userid != null) {
            val ref = Firebase.database.getReference("Menus").child("Promociones")
            options.setQuery(ref, Promociones::class.java)
            options.setLifecycleOwner(this)

            adapter = PromocionesAdapter(options.build())
            recyclerView.layoutManager = LinearLayoutManager(view.context)
            recyclerView.adapter = adapter
        }

        return view
    }
}