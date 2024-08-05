package com.example.yerbabuena

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

import com.example.yerbabuena.classes.Menu


class HomeFragment : Fragment() {

    private lateinit var adapter: HomeAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {

        val database = Firebase.database
        val myRef = database.getReference("Menus/Home")

        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rcv_home_list)

        val options = FirebaseRecyclerOptions.Builder<Menu>()
        options.setQuery(myRef, Menu::class.java)
        options.setLifecycleOwner(this)

        adapter = HomeAdapter(options.build())

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

    override fun onDestroy() {
        super.onDestroy()
        adapter.stopListening()
    }
}