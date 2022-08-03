package com.example.yerbabuena.classes

import com.google.firebase.database.IgnoreExtraProperties

// Null default values create a no-argument default constructor, which is needed
// for deserialization from a DataSnapshot.
@IgnoreExtraProperties
data class Usuario(
    var name: String? = null,
    var surname: String? = null,
    var phone: String? = null,
    var email: String? = null,
    var role: String? = null,
    var location: Ubicacion? = null,
    var imageuri: String? = null
)