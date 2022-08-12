package com.example.yerbabuena

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.yerbabuena.classes.Pedido
import com.example.yerbabuena.menus.Inicio
import com.firebase.ui.common.ChangeEventType
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class PedidosFragment : Fragment() {

    private lateinit var adapter: PedidosAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_pedidos, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerpedidos)

        val addmore = view.findViewById<FloatingActionButton>(R.id.addmore)
        addmore.setOnClickListener {
            startActivity(Intent(requireContext(), R.layout.activity_main::class.java))
        }

        val btnToPay = view.findViewById<Button>(R.id.btnToPay)
        btnToPay.setOnClickListener {
            //var inflater = LayoutInflater.from(requireContext()).inflate(R.layout.activity_main, container, false)
            //val navigationView = inflater.findViewById<NavigationView>(R.id.navigation_view)
            //navigationView.setCheckedItem(R.id.home)

            val fragment = CheckoutView2()
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.home_content, fragment, "CHECKOUT")
                .commit()

        }
        val options = FirebaseRecyclerOptions.Builder<Pedido>()
        val userid = Firebase.auth.currentUser?.uid
        if (userid != null) {
            val ref = Firebase.database.getReference("Usuarios").child(userid).child("Pedidos")
            options.setQuery(ref, Pedido::class.java)
            options.setLifecycleOwner(this)

            adapter = PedidosAdapter(options.build())
            recyclerView.layoutManager = LinearLayoutManager(view.context)
            recyclerView.adapter = adapter

            var total = 0.0
            val totalTxt:TextView = view.findViewById(R.id.TotalPrice)
            totalTxt.text = "$total"

            ref.addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val pedido = snapshot.getValue<Pedido>()

                    if (pedido != null) {
                        total += pedido.price!!
                        totalTxt.text = "$total"
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    val pedido = snapshot.getValue<Pedido>()
                    val totalTxt:TextView = view.findViewById(R.id.TotalPrice)
                    if (pedido != null) {
                        total -= pedido.price!!
                        totalTxt.text = "$total"
                    }
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        }
        return view
    }
}