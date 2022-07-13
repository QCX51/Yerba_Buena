package com.example.yerbabuena

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        lateinit var homeFragment: HomeFragment
        lateinit var mainFragment: MainFragment
        lateinit var pedidosFragment: PedidosFragment
        lateinit var promocionesFragment: PromocionesFragment
        lateinit var notificacionesFragment: NotificacionesFragment

        val toolbar: Toolbar = findViewById<Toolbar>(R.id.toolbar);
        setSupportActionBar(findViewById<Toolbar>(R.id.toolbar))

        val navigationView = findViewById<NavigationView>(R.id.navigation_view)
        val menuItem = navigationView.menu.getItem(0);
        menuItem.isChecked = true;

        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.inicio -> {

                    title="Inicio"
                    homeFragment =HomeFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.home_content, homeFragment )
                        .commit()
                    onBackPressed()
                    true
                }
                R.id.menu -> {
                    title="Menú"
                    mainFragment=MainFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.home_content, mainFragment )
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                    onBackPressed()
                    true
                }
                R.id.promociones -> {
                    title="Promociones"
                    promocionesFragment =PromocionesFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.home_content, promocionesFragment )
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                    onBackPressed()
                    true
                }
                R.id.pedidos -> {
                    title="Pedidos"
                    pedidosFragment =PedidosFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.home_content, pedidosFragment )
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                    onBackPressed()
                    true
                }
                R.id.notificaciones -> {
                    title="Notificaciones"
                    notificacionesFragment =NotificacionesFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.home_content, notificacionesFragment )
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                    onBackPressed()
                    true
                }
                R.id.cerrarsesion -> {
                    title = "Cerrar Sesión"
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
        val layoubs = drawerLayout as DrawerLayout?
        if (layoubs != null) {
            if (layoubs.isDrawerOpen(GravityCompat.START)) {
                layoubs.closeDrawer(GravityCompat.START)
            } else {
                super.onBackPressed()
            }
        }
    }
}
