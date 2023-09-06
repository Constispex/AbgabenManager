package de.medieninformatik.abgabenmanager.views

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import de.medieninformatik.abgabenmanager.R
import de.medieninformatik.abgabenmanager.model.Abgabe
import org.json.JSONArray
import org.json.JSONObject

/**
 * Die Klasse dient als Fragment für die Show Abgabe Seite.
 */
class ShowAbgabeFragment : Fragment() {

    private var id: Int = -1

    companion object {
        fun newInstance(abgabeID: Int): ShowAbgabeFragment {
            val fragment = ShowAbgabeFragment()
            val args = Bundle()
            args.putInt("abgabeID", abgabeID)
            fragment.arguments = args
            return fragment
        }
    }

    /**
     * Die Methode onCreateView() wird aufgerufen, wenn das Fragment erstellt wird.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id = arguments?.getInt("abgabeID", -1) ?: -1
    }

    /**
     * Die Methode onCreateView() wird aufgerufen, wenn das Fragment erstellt wird.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.abgabe_show_single, container, false)
        val abgabe = getAbgabe(id)
        if (abgabe.id == -1) {
            Log.e("Abgabe", "Abgabe $id not found")
            return null
        }

        val textFieldName: TextView? = view?.findViewById(R.id.textName)
        val textFieldDate: TextView? = view?.findViewById(R.id.textDate)
        val textFieldDescription: TextView? = view?.findViewById(R.id.textDescription)

        val buttonDelete: Button = view.findViewById(R.id.button_delete)

        buttonDelete.setOnClickListener {
            val sharedPreferences =
                context?.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
            val jsonString = sharedPreferences?.getString("json", "[]")
            val jsonArray = JSONArray(jsonString)
            val newJsonArray = JSONArray()

            for (i in 0 until jsonArray.length()) {
                val currJsonString = jsonArray.getString(i)
                try {
                    val currJsonObj = JSONObject(currJsonString)
                    if (currJsonObj.getInt("id") != id) {
                        newJsonArray.put(currJsonObj)
                    }
                } catch (e: Exception) {
                    Log.d("JSON", "Error with JSON: $e")
                }
            }

            sharedPreferences?.edit()?.putString("json", newJsonArray.toString())?.apply()

            val homeFragment = HomeFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frameLayout, homeFragment)
            transaction.commit()
        }

        val buttonCancel: Button = view.findViewById(R.id.button_close)
        buttonCancel.setOnClickListener {
            val homeFragment = HomeFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frameLayout, homeFragment)
            transaction.commit()

        }

        textFieldName?.text = abgabe.name
        textFieldDate?.text = abgabe.date
        textFieldDescription?.text = abgabe.description
        Log.i("Abgabe", "Abgabe $id was selected")
        return view
    }

    /**
     * Die Methode getAbgabe() gibt die Abgabe mit der übergebenen ID zurück.
     */
    private fun getAbgabe(id: Int): Abgabe {
        val sharedPreferences = context?.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val jsonString = sharedPreferences?.getString("json", "[]")
        val jsonArray = JSONArray(jsonString)

        for (i in 0 until jsonArray.length()) {
            val currJsonString = jsonArray.getString(i)
            try {
                val currJsonObj = JSONObject(currJsonString)
                if (currJsonObj.getInt("id") == id) {
                    return Abgabe(
                        currJsonObj.getInt("id"),
                        currJsonObj.getString("name"),
                        currJsonObj.getString("date"),
                        currJsonObj.getString("description"),
                        currJsonObj.getBoolean("isDone")
                    )
                }
            } catch (e: Exception) {
                Log.d("JSON", "Error with JSON: $e")
            }
        }
        return Abgabe(-1, "", "", "", false)
    }
}