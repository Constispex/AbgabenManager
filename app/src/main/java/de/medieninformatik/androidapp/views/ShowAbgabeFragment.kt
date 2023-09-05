package de.medieninformatik.androidapp.views

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import de.medieninformatik.abgabenmanager.model.Abgabe
import de.medieninformatik.androidapp.R
import org.json.JSONArray
import org.json.JSONObject

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id = arguments?.getInt("abgabeID", -1) ?: -1
        Log.i("id", "id: $id")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i("id", "id: $id")
        val view = inflater.inflate(R.layout.abgabe_show_single, container, false)
        val abgabe = getAbgabe(id)
        if (abgabe.id == -1) {
            Log.e("Abgabe", "Abgabe $id not found")
            return null
        }

        val textFieldName: TextView? = view?.findViewById(R.id.textName)
        val textFieldDate: TextView? = view?.findViewById(R.id.textDate)
        val textFieldDescription: TextView? = view?.findViewById(R.id.textDescription)

        textFieldName?.text = abgabe.name
        textFieldDate?.text = abgabe.date
        textFieldDescription?.text = abgabe.description
        Log.i("Abgabe", "Abgabe $id was selected")
        return view
    }

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