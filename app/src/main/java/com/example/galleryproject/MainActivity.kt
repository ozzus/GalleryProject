package com.example.galleryproject

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var bottomNavView: BottomNavigationView
    private lateinit var fragmentContainer: View
    private lateinit var toolbar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.top_app_bar)
        setSupportActionBar(toolbar)

        bottomNavView = findViewById(R.id.bottom_nav)
        fragmentContainer = findViewById(R.id.nav_host_fragment)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        setupActionBarWithNavController(navController)
        NavigationUI.setupWithNavController(bottomNavView, navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.welcomeFragment,
                R.id.loginFragment,
                R.id.registerFragment -> {
                    bottomNavView.visibility = View.GONE
                    fragmentContainer.setPadding(0, 0, 0, 0)
                    supportActionBar?.hide()
                }
                else -> {
                    bottomNavView.visibility = View.VISIBLE
                    fragmentContainer.setPadding(0, 0, 0, bottomNavView.height)
                    supportActionBar?.show()
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
