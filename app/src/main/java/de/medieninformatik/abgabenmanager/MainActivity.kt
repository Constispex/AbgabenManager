package de.medieninformatik.abgabenmanager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import de.medieninformatik.abgabenmanager.views.AddFragment
import de.medieninformatik.abgabenmanager.views.CalendarFragment
import de.medieninformatik.abgabenmanager.views.HomeFragment

/**
 * Die MainActivity ist die Hauptklasse der App. Sie ist für die Navigation zwischen den Fragmentem zuständig.
 */
class MainActivity : AppCompatActivity() {
    /**
     * Die onCreate Methode wird beim Start der App aufgerufen.
     * Sie setzt das HomeFragment als Startfragment initialisiert die BottomNavigationView und setzt die Listener für die Navigation.
     */
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