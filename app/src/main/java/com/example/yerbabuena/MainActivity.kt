package com.example.yerbabuena

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.yerbabuena.classes.Usuario
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.material.navigation.NavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout:DrawerLayout

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(findViewById(R.id.toolbar))
        drawerLayout = findViewById(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        toggle.syncState()
    }

    private fun switchNavMenu(menuId: Int, role: String?, name: String?)
    {
        val navigationView = findViewById<NavigationView>(R.id.navigation_view)
        navigationView.inflateMenu(menuId)
        val view = navigationView.inflateHeaderView(R.layout.main_nav_header)
        var headerTitle = view.findViewById<TextView>(R.id.header_title)
        var headerSubtitle = view.findViewById<TextView>(R.id.header_subtitle)

        // Modifica el titulo y subtitulo del encabezado segun el role correspodiente
        headerTitle.text = role
        headerSubtitle.text = name?.replaceAfter(" ", "")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        FirebaseApp.initializeApp(this)
        val firebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(
            DebugAppCheckProviderFactory.getInstance()
        )

        val navigationView = findViewById<NavigationView>(R.id.navigation_view)
        // Limpia el menu de navegacion actual
        navigationView.menu.clear()
        // Remueve el encabezado del menu de navegacion
        navigationView.removeHeaderView(navigationView.getHeaderView(0))

        val ref = Firebase.database.getReference("/Usuarios")
        ref.child(Firebase.auth.currentUser!!.uid).get().addOnSuccessListener {
            var usuario = it.getValue<Usuario>()
            if (usuario != null &&
                usuario.role?.contains("Administrador", true) == true) {
                switchNavMenu(R.menu.administrador_navigation_drawer, usuario.role, usuario.name)
            }
            else if (usuario != null &&
                usuario.role?.contains("Repartidor", true) == true) {
                switchNavMenu(R.menu.repartidor_navigation_drawer, usuario.role, usuario.name)
            }
            else {
                val navigationView = findViewById<NavigationView>(R.id.navigation_view)
                navigationView.inflateMenu(R.menu.client_navigation_drawer)
                val view = navigationView.inflateHeaderView(R.layout.nav_header)
            }
        }

        val fragment = HomeFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.home_content, fragment, "HOME")
            .commit()
        navigationView.setCheckedItem(R.id.home)

        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                /**
                 * Cliente
                 */
                R.id.home -> {
                    switchFragment(HomeFragment())
                    title = it.title
                    true
                }
                R.id.menu -> {
                    switchFragment(MenuFragment())
                    title = it.title
                    true
                }
                R.id.promotions -> {
                    switchFragment(PromocionesFragment())
                    title = it.title
                    true
                }
                R.id.orders -> {
                    switchFragment(PedidosFragment())
                    title = it.title
                    true
                }
                R.id.notifications -> {
                    switchFragment(NotificacionesFragment())
                    title = it.title
                    true
                }
                /**
                 * Administrador
                 */
                R.id.admin_products -> {
                    switchFragment(ProductosFragment())
                    title = it.title
                    true
                }
                R.id.admin_employers -> {
                    //switchFragment(PersonalFragment())
                    title = it.title
                    true
                }
                R.id.admin_clients -> {
                    View(ClientesFragment())
                    title = it.title
                    true
                }
                R.id.admin_reports -> {
                    switchFragment(ReportesFragment())
                    title = it.title
                    true
                }
                /**
                 * Repartidor
                 */
                R.id.orderlist -> {
                    switchFragment(PedidosListFragment())
                    title = it.title
                    true
                }
                R.id.location -> {
                    switchFragment(UbicacionFragment())
                    title = it.title
                    true
                }
                /**
                 * Cerrar sesion
                 */
                R.id.logout -> {
                    title = it.title
                    logout()
                    onBackPressed()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }

    private fun switchFragment(fragment: Fragment)
    {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.home_content, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
        onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.toolbar_main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun logout() {
        // Firebase sign out
        Firebase.auth.signOut()
        // Google sign out
        val signInClient = Identity.getSignInClient(this)
        signInClient.signOut().addOnCompleteListener {
            Toast.makeText(this, "Logout success", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Logout failed", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed()
    {
        when (drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            true -> { drawerLayout.closeDrawer(GravityCompat.START)}
            false -> { super.onBackPressed() }
        }
        /*val mapFragment:Fragment? = supportFragmentManager.findFragmentByTag("MAP")
        if (mapFragment != null && mapFragment.isVisible)
        {
            supportFragmentManager.beginTransaction().remove(mapFragment).commitAllowingStateLoss()
        }*/
    }
}