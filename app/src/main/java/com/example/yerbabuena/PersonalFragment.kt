package com.example.yerbabuena

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.yerbabuena.classes.Usuario
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class PersonalFragment : Fragment () {

    private lateinit var adapter: PersonalAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_personal, container, false)
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        val database = Firebase.database
        val myRef = database.getReference("Usuarios/Personal")

        // Inflate the layout for this fragment
        val recyclerView = view.findViewById<RecyclerView>(R.id.rcv_personal)

        val options = FirebaseRecyclerOptions.Builder<Usuario>()
        options.setQuery(myRef, Usuario::class.java)
        options.setLifecycleOwner(this)

        adapter = PersonalAdapter(options.build())

        recyclerView.layoutManager = LinearLayoutManager (view.context)
        recyclerView.adapter = adapter
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter.stopListening()
    }
}