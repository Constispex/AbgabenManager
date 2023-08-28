package de.medieninformatik.androidapp.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import de.medieninformatik.abgabenmanager.model.Abgabe
import de.medieninformatik.androidapp.R
import de.medieninformatik.androidapp.model.AbgabeAdapter

class HomeFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.home_fragment, container, false)

        val listView: ListView = view.findViewById(R.id.listView)

        val adapter = this.context?.let { AbgabeAdapter(it, getAbgaben()) }
        listView.adapter = adapter

        return view
    }

    private fun getAbgaben(): MutableList<Abgabe> {

            val abgaben = mutableListOf<Abgabe>()
            abgaben.add(Abgabe(1, "Abgabe 1", "01.01.2021", "MCI", listOf(), "Beschreibung 1", false))
            abgaben.add(Abgabe(2, "Abgabe 2", "02.01.2021", "MCI", listOf(), "Beschreibung 2", false))
            abgaben.add(Abgabe(3, "Abgabe 3", "03.01.2021", "MCI", listOf(), "Beschreibung 3", false))

            return abgaben
    }
}
