package com.example.yerbabuena

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navigationView = findViewById<NavigationView>(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener {
            selectDrawerItem(it)
            true
        }

        drawerLayout = findViewById(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(this,
            drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navigationView.setCheckedItem(R.id.inicio)
        val fragment = MapsFragment.newInstance("maps", "")
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.home_content, fragment)
            .commit()
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
    private fun selectDrawerItem(it:MenuItem)
    {
        when (it.itemId) {
            R.id.inicio -> {
                title = it.title
                val fragment = MapsFragment.newInstance("maps", "")
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.home_content, fragment)
                    .commit()
                onBackPressed()
            }
            R.id.menu -> {
                title = it.title
                val fragment = fragment_menu.newInstance("menu", "")
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.home_content, fragment)
                    .commit()
                onBackPressed()
            }
            R.id.promociones -> {
                title = it.title
                onBackPressed()
            }
            R.id.pedidos -> {
                title = it.title
                onBackPressed()
            }
            R.id.notificaciones -> {
                title = it.title
                onBackPressed()
            }
            R.id.cerrarsesion -> {
                title = it.title
                onBackPressed()
            }
        }
    }
}
