package com.example.yerbabuena.classes

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Ubicacion(
    var latitud: Double? = null,
    var longitud: Double? = null
)