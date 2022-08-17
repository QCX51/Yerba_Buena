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
    var location: Ubicacion? = null,
    var thumbnail: String? = null,
    var orders: Pedido? = null
)
{
    fun getUsername(): String {
        return "$name $surname"
    }
    fun getPhoneNumber(): String {
        return "$phone"
    }
    fun getEmailAddress(): String {
        return "$email"
    }
}