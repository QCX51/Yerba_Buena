package com.example.yerbabuena

import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navigationView = findViewById<NavigationView>(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener(this)

        drawerLayout = findViewById(R.id.drawer_layout)
        toggle = ActionBarDrawerToggle(this,
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    // synchronize the state whenever the screen is restored or there is a configuration change (i.e screen rotation)
    override fun onPostCreate(savedInstanceState: Bundle?) {
        // Sync the toggle state after onRestoreInstanceState has occurred.
        toggle.syncState()
        super.onPostCreate(savedInstanceState)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        // Pass any configuration change to the drawer toggles
        toggle.onConfigurationChanged(newConfig)
        super.onConfigurationChanged(newConfig);
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean
    {
        title = item.title
        when (item.itemId) {
            R.id.inicio -> {
                val fragment = MapsFragment.newInstance("maps", "")
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.home_content, fragment)
                    .commit()
                onBackPressed()
                return true
            }
            R.id.menu -> {
                val fragment = fragment_menu.newInstance("menu", "")
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.home_content, fragment)
                    .commit()
                onBackPressed()
                return true
            }
            R.id.promociones -> {
                onBackPressed()
                return true
            }
            R.id.pedidos -> {
                onBackPressed()
                return true
            }
            R.id.notificaciones -> {
                onBackPressed()
                return true
            }
            R.id.cerrarsesion -> {
                onBackPressed()
                return true
            }
        }
        return false
    }
    override fun onBackPressed() {
        when (this.drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            true ->
            {
                this.drawerLayout.closeDrawer(GravityCompat.START)
            }
            else ->
            {
                super.onBackPressed()
            }
        }
    }
}
