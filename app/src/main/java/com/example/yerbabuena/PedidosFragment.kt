package com.example.yerbabuena

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.os.Message
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.yerbabuena.classes.Notificacion
import com.example.yerbabuena.classes.Pedido
import com.firebase.ui.common.ChangeEventType
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.database.ktx.snapshots
import com.google.firebase.ktx.Firebase
import java.util.concurrent.Flow

class PedidosFragment : Fragment() {
    private lateinit var btnToPay: Button
    private lateinit var amount: TextView

    fun buttonEnable() {
        btnToPay.visibility = View.VISIBLE
        btnToPay.isEnabled = true
    }

    fun updateAmount(amount: Double) {
        this.amount.text = resources.getString(R.string.TotalPrice, amount)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view: View = inflater.inflate(R.layout.fragment_pedidos, container, false)
        amount = view.findViewById(R.id.textViewTotal)
        val btnBack = view.findViewById<Button>(R.id.btnBack)
        btnToPay = view.findViewById(R.id.btnNext)
        amount.text = resources.getString(R.string.TotalPrice, 0.0)
        var step = 0

        //val v = LayoutInflater.from(requireContext()).inflate(R.layout.activity_main, container, false)

        btnBack.setOnClickListener {
            btnToPay.text = "Continuar"
            val navController = Navigation.findNavController(requireActivity(), R.id.fragmentContainerView2)
            when (step) {
                1 -> {
                    navController.navigate(R.id.pedidosRcvFragment)
                    btnBack.visibility = View.INVISIBLE
                    btnBack.isEnabled = false

                    btnToPay.text = "Siguiente"
                    btnToPay.visibility = View.VISIBLE
                    btnToPay.isEnabled = true

                    step -= 1
                }
                2 -> {
                    navController.navigate(R.id.checkoutView2)

                    btnToPay.text = "Siguiente"
                    btnToPay.visibility = View.VISIBLE
                    btnToPay.isEnabled = true
                    step -= 1
                }
            }
        }

        btnToPay.setOnClickListener {
            when (step) {
                0 -> {
                    val bundle = Bundle()
                    bundle.putDouble("amount", 0.0)
                    Navigation.findNavController(requireActivity(), R.id.fragmentContainerView2)
                        .navigate(R.id.checkoutView2, bundle)
                    btnBack.visibility = View.VISIBLE
                    btnBack.isEnabled = true
                    step += 1
                }
                1 -> {
                    val bundle = Bundle()
                    /*bundle.putString("key", key)*/
                    Navigation.findNavController(requireActivity(), R.id.fragmentContainerView2)
                        .navigate(R.id.ubicacionFragment, bundle)
                    btnToPay.text = "Confirmar"
                    btnToPay.visibility = View.INVISIBLE
                    btnToPay.isEnabled = false
                    step += 1
                }
                2 -> {
                    val ref = Firebase.database.getReference("Usuarios")
                    ref.get().addOnCompleteListener { DataSnapshot ->
                        if (DataSnapshot.isSuccessful) {
                            DataSnapshot.result.children.forEach { Snapshot ->
                                Toast.makeText(requireContext(), Snapshot.key, Toast.LENGTH_LONG)
                                    .show()
                            }
                        }
                        /*Map<String, Object> updates = new HashMap<String, Object>();
                        updates["/path/to/new/value"] = dataSnapshot.getValue();
                        updates["/path/to/old/value"] = null;
                        rootRef.updateChildren(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Constant.print("REMOVED: " + fromPath.getKey());
                            }
                        });
                    });*/
                    }

                    val navigationView = activity?.findViewById<NavigationView>(R.id.navigation_view)
                    navigationView?.setCheckedItem(R.id.home)
                    parentFragmentManager
                        .beginTransaction()
                        .replace(R.id.home_content, HomeFragment())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }
            }
        }
        return view
    }
}