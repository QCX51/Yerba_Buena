package com.example.yerbabuena.classes

import com.google.firebase.database.IgnoreExtraProperties


@IgnoreExtraProperties
data class Notificacion (
    val title: String? = null,
    val subtitle: String? = null,
    val body: String? = null,
    val seen: Boolean? = null,
    val show: Boolean? = null,
    val timestamp: String? = null // DateTimeFormatter.ISO_INSTANT.format(Instant.now())
)