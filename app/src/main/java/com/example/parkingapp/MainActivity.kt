package com.example.parkingapp

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.view.menu.MenuView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.menuapplication.loadConnexion
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        if (!loadConnexion(this)) {
            val intent = Intent(this@MainActivity, SignActivity::class.java)
            startActivity(intent)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_main_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.parksCardFragment,R.id.parkingsList_Fragment),
            findViewById<DrawerLayout>(R.id.drawer_layout)
        )

        setSupportActionBar(findViewById(R.id.main_toolbar))
        setupActionBarWithNavController(navController,appBarConfiguration)

        findViewById<BottomNavigationView>(R.id.bottom_nav).setupWithNavController(navController)
        findViewById<NavigationView>(R.id.nav_view).setupWithNavController(navController)



    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}
