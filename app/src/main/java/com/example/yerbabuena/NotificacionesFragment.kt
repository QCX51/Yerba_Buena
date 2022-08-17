package com.example.yerbabuena
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.yerbabuena.classes.Menu
import com.example.yerbabuena.classes.Notificacion
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class NotificacionesFragment : Fragment() {
    
    private lateinit var adapter: FirebaseRecyclerAdapter<Notificacion, ViewHolder>
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView
        var subtitle: TextView
        var body: TextView

        init {
            title = itemView.findViewById(R.id.notification_title)
            subtitle = itemView.findViewById(R.id.notification_subtitle)
            body = itemView.findViewById(R.id.notification_body)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_notificaciones, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rcv_notifications)

        var userid = Firebase.auth.currentUser!!.uid
        val ref = Firebase.database.getReference("Usuarios").child(userid).child("Notificaciones")
        val options = FirebaseRecyclerOptions.Builder<Notificacion>()
        options.setQuery(ref, Notificacion::class.java)
        options.setLifecycleOwner(this)

        adapter = object : FirebaseRecyclerAdapter<Notificacion, ViewHolder>(options.build()) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                return ViewHolder(LayoutInflater.from(parent.context)
                        .inflate(R.layout.card_notifications, parent, false))
            }
            override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Notificacion) {
                holder.title.setText(R.string.app_name)
                holder.subtitle.text = model.subtitle
                holder.body.text = model.body

                holder.itemView.setOnClickListener {
                    it.visibility = View.INVISIBLE
                    it.layoutParams.height = 0
                }
            }
        }
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.adapter = adapter
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter.stopListening()
    }
}