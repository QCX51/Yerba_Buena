package com.example.yerbabuena.menus


class Inicio {
    private lateinit var name: String
    private lateinit var description:String
    private var price: Double = 0.0

    fun getName(): String { return name }
    fun getDescription(): String { return description }
    fun getPrice(): Double { return price }
}