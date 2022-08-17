package com.example.yerbabuena

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.yerbabuena.classes.Menu
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ProductosFragment : Fragment() {

    private var recyclerView:RecyclerView? = null
    private var homerecyclerView:RecyclerView? = null
    private var promrecyclerView:RecyclerView? = null
    private var gridLayoutManager:GridLayoutManager? = null
    private lateinit var adapter: ProductosAdapter


    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,  savedInstanceState: Bundle? ): View? {
        val view: View = inflater.inflate(R.layout.fragment_productos, container, false)
        gridLayoutManager = GridLayoutManager (requireContext(), 3, LinearLayoutManager.VERTICAL, false)
        recyclerView = view.findViewById(R.id.rcv_menu)
        homerecyclerView = view.findViewById(R.id.rcv_home)
        promrecyclerView = view.findViewById(R.id.rcv_promociones)

        val database = Firebase.database

        var ref = database.getReference("Menus/Menu")
        var options = FirebaseRecyclerOptions.Builder<Menu>()
        options.setQuery(ref, Menu::class.java)
        options.setLifecycleOwner(this)
        adapter = ProductosAdapter(options.build())
        recyclerView?.layoutManager = gridLayoutManager
        recyclerView?.autoFitColumns(160)
        recyclerView?.adapter = adapter

        ref = database.getReference("Menus/Home")
        options = FirebaseRecyclerOptions.Builder<Menu>()
        options.setQuery(ref, Menu::class.java)
        options.setLifecycleOwner(this)
        adapter = ProductosAdapter(options.build())
        gridLayoutManager = GridLayoutManager (requireContext(), 3, LinearLayoutManager.VERTICAL, false)
        homerecyclerView?.layoutManager = gridLayoutManager
        homerecyclerView?.autoFitColumns(160)
        homerecyclerView?.adapter = adapter


        ref = database.getReference("Menus/Promociones")
        options = FirebaseRecyclerOptions.Builder<Menu>()
        options.setQuery(ref, Menu::class.java)
        options.setLifecycleOwner(this)
        adapter = ProductosAdapter(options.build())
        gridLayoutManager = GridLayoutManager (requireContext(), 3, LinearLayoutManager.VERTICAL, false)
        promrecyclerView?.layoutManager = gridLayoutManager
        promrecyclerView?.autoFitColumns(160)
        promrecyclerView?.adapter = adapter

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter.stopListening()
    }

    /**
     * @param columnWidth - in dp
     */
    fun RecyclerView.autoFitColumns(columnWidth: Int) {
        val displayMetrics = this.context.resources.displayMetrics
        val noOfColumns = ((displayMetrics.widthPixels / displayMetrics.density) / columnWidth).toInt()
        this.layoutManager = GridLayoutManager(this.context, noOfColumns)
    }
}