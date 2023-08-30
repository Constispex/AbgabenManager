package de.medieninformatik.androidapp.views

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import de.medieninformatik.abgabenmanager.model.Abgabe
import de.medieninformatik.androidapp.R
import de.medieninformatik.androidapp.model.AbgabeAdapter
import org.json.JSONObject
import java.io.FileReader

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
        abgaben.add(Abgabe(3, "Abgabe 33", "03.01.2021", "MCI", listOf(), "Beschreibung 3", false))

        val sharedPreferences = context?.getSharedPreferences("json", Context.MODE_PRIVATE)
        val gson = Gson()

        val jsonString = sharedPreferences?.getString("json", "")

        if (jsonString != "" && jsonString != null) {
            val jsonObject = JSONObject(jsonString)
            Log.d("JSON", jsonObject.toString())

            val jsonLength= jsonObject.length()
            for (i in 0 until jsonLength) {
                val abgabe = gson.fromJson(jsonObject.getJSONObject(i.toString()).toString(), Abgabe::class.java)
                abgaben.add(abgabe)
            }

        } else {
            Log.d("JSON", "No JSON found")
        }


        return abgaben
    }
}
