package com.example.yerbabuena

import android.content.Intent
import android.os.Bundle
import android.view.Menu
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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout:DrawerLayout

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        val toolbar: Toolbar = findViewById<Toolbar>(R.id.toolbar);
        setSupportActionBar(findViewById<Toolbar>(R.id.toolbar))
        drawerLayout = findViewById(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        toggle.syncState()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        lateinit var homeFragment: HomeFragment
        lateinit var menuFragment: MenuFragment
        lateinit var pedidosFragment: PedidosFragment
        lateinit var promocionesFragment: PromocionesFragment
        lateinit var notificacionesFragment: NotificacionesFragment

        val navigationView = findViewById<NavigationView>(R.id.navigation_view)

        val ref = Firebase.database.getReference("/Usuarios")
        ref.child(Firebase.auth.currentUser!!.uid).get().addOnSuccessListener {
            var usuario = it.getValue<Usuario>()
            Toast.makeText(this, "${usuario?.role}", Toast.LENGTH_SHORT).show()
            if (usuario != null && usuario.role == "Administrador")
            {
                // Limpia el menu de navegacion actual para mostrar la del repartidor | administrador
                navigationView.menu.clear()
                navigationView.inflateMenu(R.menu.administrador_navigation_drawer)
                // Remueve el encabezado del cliente para poder mostar la del repartidor | administrador
                navigationView.removeHeaderView(navigationView.getHeaderView(0))
                val view = navigationView.inflateHeaderView(R.layout.main_nav_header)
                var headerTitle = view.findViewById<TextView>(R.id.header_title)
                var headerSubtitle = view.findViewById<TextView>(R.id.header_subtitle)

                // Modifica el titulo y subtitulo del encabezado segun el role correspodiente
                headerTitle.text = usuario.role
                headerSubtitle.text = Firebase.auth.currentUser!!.displayName
            }
        }

        //var fragment = HomeFragment()
        val fragment = MapsFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.home_content, fragment, "MAP")
            .commit()
        navigationView.setCheckedItem(R.id.home)

        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
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
                R.id.test ->
                {
                    switchFragment(CheckoutView2())
                    title = it.title
                    true
                }
                // **** Administrador ****
                R.id.admin_products -> {
                    //switchFragment(ProductosFragment())
                    title = it.title
                    true
                }
                R.id.admin_employers -> {
                    //switchFragment(PersonalFragment())
                    title = it.title
                    true
                }
                R.id.admin_promotions -> {
                    //switchFragment(PromocionesFragment())
                    title = it.title
                    true
                }
                R.id.admin_clients -> {
                    //switchFragment(ClientesFragment())
                    title = it.title
                    true
                }
                R.id.admin_reports -> {
                    //switchFragment(ReportesFragment())
                    title = it.title
                    true
                }
                // ************
                R.id.logout -> {
                    title = it.title
                    logout()
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