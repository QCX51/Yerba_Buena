package com.example.yerbabuena

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout:DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        lateinit var homeFragment: HomeFragment
        lateinit var menuFragment: MenuFragment
        lateinit var pedidosFragment: PedidosFragment
        lateinit var promocionesFragment: PromocionesFragment
        lateinit var notificacionesFragment: NotificacionesFragment

        val toolbar: Toolbar = findViewById<Toolbar>(R.id.toolbar);
        setSupportActionBar(findViewById<Toolbar>(R.id.toolbar))
        val navigationView = findViewById<NavigationView>(R.id.navigation_view)

        drawerLayout = findViewById(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        toggle.syncState()

        Toast.makeText(this, "Hola: " + Firebase.auth.currentUser?.displayName, Toast.LENGTH_SHORT).show()

        //homeFragment = HomeFragment()
        var fragment:MapsFragment = MapsFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.home_content, fragment )
            .commit()
        navigationView.setCheckedItem(R.id.inicio)

        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.inicio -> {
                    title = it.title
                    homeFragment = HomeFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.home_content, homeFragment )
                        .commit()
                    onBackPressed()
                    true
                }
                R.id.menu -> {
                    title = it.title
                    menuFragment= MenuFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.home_content, menuFragment )
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                    onBackPressed()
                    true
                }
                R.id.promociones -> {
                    title = it.title
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
                    title = it.title
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
                    title = it.title
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
                    title = it.title
                    signOut()
                    onBackPressed()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                else -> false
            }
        }
    }

    override fun onStart() {
        super.onStart()
    }

    private fun signOut() {
        // Firebase sign out
        Firebase.auth.signOut()
        // Google sign out
        val signInClient = Identity.getSignInClient(this)
        signInClient.signOut().addOnCompleteListener {
            Toast.makeText(this, "SignOut Success", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        when (drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            true -> { drawerLayout.closeDrawer(GravityCompat.START)}
            false -> { super.onBackPressed() }
        }
    }
}