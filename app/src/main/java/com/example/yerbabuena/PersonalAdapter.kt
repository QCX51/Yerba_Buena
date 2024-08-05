package com.example.yerbabuena
import android.provider.Settings.Global.getString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import com.example.yerbabuena.classes.Usuario
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class PersonalAdapter(options: FirebaseRecyclerOptions<Usuario>) : FirebaseRecyclerAdapter<Usuario, PersonalAdapter.ViewHolder>(options) {


    private lateinit var view: ViewGroup
    override fun onDataChanged() {
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var UserIcon: ImageView
        var UserName: TextView
        var UserPhone: TextView
        var UserEmail: TextView
        var view: View

        init {
            UserIcon = itemView.findViewById(R.id.personal_icon)
            UserName = itemView.findViewById(R.id.username)
            UserPhone = itemView.findViewById(R.id.userphone)
            UserEmail = itemView.findViewById(R.id.useremail)
            view = itemView
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Usuario) {

            holder.UserName.text = model.getUsername()
            holder.UserPhone.text = model.getPhoneNumber()
            holder.UserEmail.text = model.getEmailAddress()
            holder.UserIcon?.setImageResource(R.drawable.alara_alvarez)
            //holder.view.visibility = View.GONE
            //holder.view.layoutParams.height = 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.card_personal, parent, false))
        view = parent
        return v
    }
}