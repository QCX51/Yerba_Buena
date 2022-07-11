package com.example.yerbabuena

import android.os.Bundle
import android.view.MenuItem
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
        val menuItem = navigationView.getMenu().getItem(0);
        menuItem.setChecked(true);

        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.inicio -> {
                    setTitle("Inicio")
                    onBackPressed()
                    true
                }
                R.id.menu -> {
                    setTitle("Menu")
                    onBackPressed()
                    true
                }
                R.id.promociones -> {
                    setTitle("Promociones")
                    onBackPressed()
                    true
                }
                R.id.pedidos -> {
                    setTitle("Pedidos")
                    onBackPressed()
                    true
                }
                R.id.notificaciones -> {
                    setTitle("Notificaciones")
                    onBackPressed()
                    true
                }
                R.id.cerrarsesion -> {
                    setTitle("Salir")
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
        val layoutf = drawerLayout as DrawerLayout?
        if (layoutf != null) {
            if (layoutf.isDrawerOpen(GravityCompat.START)) {
                layoutf.closeDrawer(GravityCompat.START)
            } else {
                super.onBackPressed()
            }
        }
    }
}
