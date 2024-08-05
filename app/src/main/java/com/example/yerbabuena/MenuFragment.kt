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
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MenuFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val database = Firebase.database
        val myRef = database.getReference("Menus/Menu")

        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_menu, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rcv_menu_list)

        val options = FirebaseRecyclerOptions.Builder<Menu>()
        options.setQuery(myRef, Menu::class.java)
        options.setLifecycleOwner(this)

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
        /*val adapter = object : FirebaseRecyclerAdapter<Menus, ChatHolder>(options) {}*/
        recyclerView.layoutManager = layout
        recyclerView.adapter = MenuAdapter(options.build())
        return view
    }
}