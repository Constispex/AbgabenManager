package de.medieninformatik.abgabenmanager.views

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import de.medieninformatik.abgabenmanager.R
import de.medieninformatik.abgabenmanager.model.Abgabe
import de.medieninformatik.abgabenmanager.model.AbgabeAdapter
import org.json.JSONArray
import org.json.JSONObject

/**
 * Die Klasse dient als Fragment für die Home Seite.
 */
class HomeFragment : Fragment() {
    /**
     * Die Methode onCreateView() wird aufgerufen, wenn das Fragment erstellt wird.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.home_fragment, container, false)
        val listView: ListView = view.findViewById(R.id.listView)
        val adapter = this.context?.let { AbgabeAdapter(it, getAbgaben()) }
        listView.adapter = adapter
        return view
    }

    /**
     * Die Methode getAbgaben() gibt alle Abgaben zurück, die in der SharedPreferences gespeichert sind.
     */
    private fun getAbgaben(): MutableList<Abgabe> {
        val abgaben = mutableListOf<Abgabe>()
        val sharedPreferences = context?.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)

        val jsonString = sharedPreferences?.getString("json", "[]")
        val jsonArray = JSONArray(jsonString)

        for (i in 0 until jsonArray.length()) {
            val currJsonString = jsonArray.getString(i)
            try {
                val currJsonObj = JSONObject(currJsonString)
                val curr = Abgabe(
                    currJsonObj.getInt("id"),
                    currJsonObj.getString("name"),
                    currJsonObj.getString("date"),
                    currJsonObj.getString("description"),
                    currJsonObj.getBoolean("isDone")
                )
                abgaben.add(curr)
            } catch (e: Exception) {
                Log.d("JSON", "Error with JSON: $e")
            }
        }
        return abgaben
    }
}
