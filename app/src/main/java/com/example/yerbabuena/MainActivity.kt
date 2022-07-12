package com.example.yerbabuena

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById<Toolbar>(R.id.toolbar);
        setSupportActionBar(findViewById<Toolbar>(R.id.toolbar))

        val navigationView = findViewById<NavigationView>(R.id.navigation_view)
        val menuItem = navigationView.menu.getItem(0);
        menuItem.isChecked = true;

        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.inicio -> {

                    title = "Inicio"
                    onBackPressed()
                    true
                }
                R.id.menu -> {
                    title = "Menu"
                    onBackPressed()
                    true
                }
                R.id.promociones -> {
                    title = "Promociones"
                    onBackPressed()
                    true
                }
                R.id.pedidos -> {
                    title = "Pedidos"
                    onBackPressed()
                    true
                }
                R.id.notificaciones -> {
                    title = "Notificaciones"
                    onBackPressed()
                    true
                }
                R.id.cerrarsesion -> {
                    title = "Salir"
                    onBackPressed()
                    true
                }
                else -> false
            }
        }

        drawerLayout = findViewById(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(this,
            drawerLayout as DrawerLayout?, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

    /*val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = CustomAdapter()

        recyclerView.layoutManager = LinearLayoutManager (this)
        recyclerView.adapter = adapter*/
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onBackPressed() {
        val layouts = drawerLayout as DrawerLayout?
        if (layouts != null) {
            if (layouts.isDrawerOpen(GravityCompat.START)) {
                layouts.closeDrawer(GravityCompat.START)
            } else {
                super.onBackPressed()
            }
        }
    }
}
