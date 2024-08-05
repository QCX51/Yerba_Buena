package com.example.yerbabuena.classes

import com.google.firebase.database.IgnoreExtraProperties

// Null default values create a no-argument default constructor, which is needed
// for deserialization from a DataSnapshot.
enum class ROLE {
    administrator,
    deliveryman,
    customer
}
@IgnoreExtraProperties
data class Usuario(
    var name: String? = null,
    var phone: String? = null,
    var email: String? = null,
    var location: Ubicacion? = null,
    var thumbnail: String? = null,
    var orders: Pedido? = null,
    var role: Int = ROLE.customer.ordinal
)
{
    fun getUsername(): String {
        return "$name"
    }
    fun getPhoneNumber(): String {
        return "$phone"
    }
    fun getEmailAddress(): String {
        return "$email"
    }
}