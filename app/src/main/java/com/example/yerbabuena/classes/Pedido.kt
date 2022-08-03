package com.example.yerbabuena.classes

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Pedido (
    var name: String? = null,
    var description: String? = null,
    var imageuri: String? = null,
    var price: Double? = null,
)