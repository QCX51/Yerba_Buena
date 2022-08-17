package com.example.yerbabuena.classes

enum class MenuType
{
    Principal,
    Promotion,
    Menu
}
class Menu {
    public var name: String? = null
    public var description: String? = null
    public var thumbnail: String? = null
    public var menuType: MenuType? = null
    public var price: Double = 0.0
    public var discount: Double = 0.0
    public var available: Boolean = false
}