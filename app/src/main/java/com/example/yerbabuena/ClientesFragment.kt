package com.example.yerbabuena

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.fragment_clientes.*

class ClientesFragment : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_clientes)


        val cliente1 = list_cliente(
            "Aurora",
        "7721451221",
        R.drawable.itemensalda

        )
        val cliente2 = list_cliente(
            "Aurora",
            "7721451221",
            R.drawable.itemensalda

        )
        val cliente3 = list_cliente(
            "Aurora",
            "7721451221",
            R.drawable.itemensalda

        )
        val cliente4 = list_cliente(
            "Aurora",
            "7721451221",
            R.drawable.itemensalda

        )
        val cliente5 = list_cliente(
            "Aurora",
            "7721451221",
            R.drawable.itemensalda

        )

val clienteList= arrayListOf(cliente1,cliente2,cliente3,cliente4,cliente5)
        val adapter =listclienteadapter(this,clienteList)
        listclientes.adapter=adapter
    }
}