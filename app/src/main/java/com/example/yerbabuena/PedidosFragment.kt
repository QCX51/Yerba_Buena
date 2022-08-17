package com.example.yerbabuena

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.yerbabuena.classes.Pedido
import com.example.yerbabuena.classes.Usuario
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.gms.common.internal.Objects
import com.google.android.gms.location.Priority
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class PedidosFragment : Fragment() {

    private lateinit var adapter: FirebaseRecyclerAdapter<Pedido, ViewHolder>
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var total: TextView
        init {
            total = itemView.findViewById(R.id.textViewTotal)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view: View = inflater.inflate(R.layout.fragment_pedidos, container, false)
        var txtTotal = view.findViewById<TextView>(R.id.textViewTotal)
        val btnBack = view.findViewById<Button>(R.id.btnBack)
        val btnToPay = view.findViewById<Button>(R.id.btnNext)

        txtTotal.text = resources.getString(R.string.TotalPrice, 0.0)
        //val v = LayoutInflater.from(requireContext()).inflate(R.layout.activity_main, container, false)
        //val view2: View = inflater.inflate(R.layout.fragment_pedidos_rcv, container, false)

        var ref = Firebase.database.getReference("Usuarios")
            .child(Firebase.auth.currentUser!!.uid).child("Pedidos")
        val key = ref.push().key

        var total = 0.0
        var step = 0

        var options = FirebaseRecyclerOptions.Builder<Pedido>()
        options.setQuery(ref, Pedido::class.java)
        options.setLifecycleOwner(this)

        adapter = object : FirebaseRecyclerAdapter<Pedido, ViewHolder>(options.build())
        {
            override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Pedido) {
                total += model.price!!
                txtTotal.text = resources.getString(R.string.TotalPrice, total)
                //Log.println(Log.ERROR, "Total", total.toString())
                Toast.makeText(requireContext(), total.toString(), Toast.LENGTH_SHORT).show()
            }
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                Toast.makeText(requireContext(), "holder", Toast.LENGTH_SHORT).show()
                return ViewHolder(view)
            }
        }

        btnBack.setOnClickListener {
            btnToPay.text = "Continuar"
            val navController = Navigation.findNavController(requireActivity(), R.id.fragmentContainerView2)
            when (step) {
                1 -> {
                    navController.navigate(R.id.pedidosRcvFragment)
                    btnBack.visibility = View.INVISIBLE
                    btnBack.isEnabled = false
                    step -= 1
                }
                2 -> {
                    navController.navigate(R.id.checkoutView2)
                    step -= 1
                }
            }
        }

        btnToPay.setOnClickListener {
            when (step) {
                0 -> {
                    val bundle = Bundle()
                    bundle.putDouble("amount", total)
                    Navigation.findNavController(requireActivity(), R.id.fragmentContainerView2)
                        .navigate(R.id.checkoutView2, bundle)
                    btnBack.visibility = View.VISIBLE
                    btnBack.isEnabled = true
                    step += 1
                }
                1 -> {
                    val bundle = Bundle()
                    bundle.putString("key", key)
                    Navigation.findNavController(requireActivity(), R.id.fragmentContainerView2)
                        .navigate(R.id.ubicacionFragment, bundle)
                    btnToPay.text = "Confirmar"
                    btnToPay.visibility = View.INVISIBLE
                    btnToPay.isEnabled = false
                    step += 1
                }
                2 -> {
                    var navigationView = activity?.findViewById<NavigationView>(R.id.navigation_view)
                    navigationView?.setCheckedItem(R.id.home)
                    parentFragmentManager
                        .beginTransaction()
                        .replace(R.id.home_content, HomeFragment())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                    step += 0
                }
            }
        }



         /*listener = ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val pedido = snapshot.getValue<Pedido>()
                if (pedido != null) {
                    total += pedido.price!!
                    txtTotal.text = resources.getString(R.string.TotalPrice, total)
                }
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {
                val pedido = snapshot.getValue<Pedido>()
                if (pedido != null) {
                    total -= pedido.price!!
                    txtTotal.text = resources.getString(R.string.TotalPrice, total)
                }
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })*/
        return view
    }
}