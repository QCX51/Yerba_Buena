package com.example.yerbabuena.menus

class Promociones {
    private lateinit var name: String
    private lateinit var description: String
    private var price:Double = 0.0
    private var lastprice = 0.0
    private lateinit var imageUri: String

    fun getName(): String { return name }
    fun getDescription(): String { return  description }
    fun getPrice(): Double { return  price }
    fun getImageUri(): String { return  imageUri }
    fun getlastprice(): Double { return  lastprice }
}