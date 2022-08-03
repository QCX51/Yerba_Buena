package com.example.yerbabuena.classes

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Notificacion (
    val title: String? = null,
    val body: String? = null,
    val seen: Boolean? = null
)