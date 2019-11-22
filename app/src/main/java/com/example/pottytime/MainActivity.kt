package com.example.pottytime

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.pottytime.ui.dashboard.DashboardFragment
import com.example.pottytime.ui.home.HomeFragment
import com.example.pottytime.ui.loginPage.LoginActivity
import com.example.pottytime.ui.loginPage.LogoutFragment
import com.example.pottytime.ui.notifications.NotificationsFragment
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.common.util.CollectionUtils.setOf
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*





class MainActivity : AppCompatActivity() {
    private var firebaseAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        val navView: BottomNavigationView = findViewById(R.id.nav_view)
//
//        val navController = findNavController(R.id.nav_host_fragment)
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
//            )
//        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)

        // Checking if user is already logged in
        firebaseAuth = FirebaseAuth.getInstance()
        val user = FirebaseAuth.getInstance().currentUser
//        if (user == null) {
//            val intent = Intent(this@MainActivity, LoginActivity::class.java)
//            startActivity(intent)
//            finish()
//        }

        // Have map fragment (homefragment) as default
        replaceFragment(HomeFragment())

        bottom_Navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListner)
    }

    private val mOnNavigationItemSelectedListner = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when(item.itemId){
            R.id.navigation_map -> {
                replaceFragment(HomeFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                replaceFragment(DashboardFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                replaceFragment(NotificationsFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_logout -> {
                replaceFragment(LogoutFragment())
                return@OnNavigationItemSelectedListener true
            }
//            R.id.navigation_notifications -> {
//                replaceFragment(IVANREVIEWFragment())
//                return@OnNavigationItemSelectedListener true
//            }
        }
        false
    }

    private fun replaceFragment(fragment : Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.commit()
    }
}
