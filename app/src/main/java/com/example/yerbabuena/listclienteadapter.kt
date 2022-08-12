package com.example.yerbabuena

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.list_cliente.view.*


class listclienteadapter(private val cContext: Context, private val listCliente: ArrayList<list_cliente>): ArrayAdapter<list_cliente>(cContext, 0,listCliente){
    @SuppressLint("ViewHolder")
    override fun getView(position:Int, convertView: View?, parent: ViewGroup): View {
        val layout = LayoutInflater.from(cContext).inflate(
            R.layout.list_cliente,
            parent,
            false
        )
        val clientes = listCliente[position]
        layout.clienteTitle.text = clientes.clienteTitle
        layout.clienteDescription.text = clientes.clienteDescription
        layout.imagecliente.setImageResource(clientes.imagecliente)

        return layout
    }

}
