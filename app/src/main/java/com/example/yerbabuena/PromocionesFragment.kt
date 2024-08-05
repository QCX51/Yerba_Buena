package com.example.yerbabuena

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.yerbabuena.classes.Menu
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class PromocionesFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_promociones, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rcv_promotions)

        val options = FirebaseRecyclerOptions.Builder<Menu>()
        val userid = Firebase.auth.currentUser?.uid
        if (userid != null) {
            val ref = Firebase.database.getReference("Menus").child("Promociones")
            options.setQuery(ref, Menu::class.java)
            options.setLifecycleOwner(this)

            val adapter = PromocionesAdapter(options.build())
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
        }
        return view
    }
}