package com.example.yerbabuena.classes

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Ubicacion(
    var latitude: Double? = null,
    var longitude: Double? = null
)