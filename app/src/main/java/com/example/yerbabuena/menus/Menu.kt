package com.example.yerbabuena.menus

class Menus {
    private lateinit var name: String
    private lateinit var description: String
    private lateinit var imageuri: String
    private var price: Double = 0.0
    private var available: Boolean = true

    fun getName(): String { return name }
    fun getDescription(): String { return  description }
    fun getPrice(): Double { return  price }
    fun getImageUri(): String { return  imageuri }
    fun isAvailable(): Boolean { return available }

    fun setName(name: String) { this.name = name }
    fun setDescription(description: String) { this.description = description }
    fun setImageUri(imageuri: String) { this.imageuri = imageuri }
    fun setAvailable(available: Boolean) { this.available = available }
}