package com.example.pottytime

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.pottytime.ui.dashboard.DashboardFragment
import com.example.pottytime.ui.home.HomeFragment
import com.example.pottytime.ui.loginPage.LogoutFragment
import com.example.pottytime.ui.notifications.NotificationsFragment
import com.example.pottytime.ui.bathroom.BathroomsFragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query





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
//            R.id.navigation_logout -> {
//                replaceFragment(LogoutFragment())
//                return@OnNavigationItemSelectedListener true
//            }
            R.id.navigation_bathrooms -> {
                replaceFragment(BathroomsFragment())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun replaceFragment(fragment : Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.commit()
    }
}
