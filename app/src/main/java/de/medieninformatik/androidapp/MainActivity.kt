package de.medieninformatik.androidapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import de.medieninformatik.androidapp.views.AddFragment
import de.medieninformatik.androidapp.views.CalendarFragment
import de.medieninformatik.androidapp.views.HomeFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, HomeFragment())
            .commit()
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, HomeFragment())
                        .commit()
                    true
                }
                R.id.menu_add -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, AddFragment())
                        .commit()
                    true
                }
                R.id.menu_calendar -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, CalendarFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }

    }


}