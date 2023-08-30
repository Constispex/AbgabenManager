package de.medieninformatik.androidapp.views

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import de.medieninformatik.abgabenmanager.model.Abgabe
import de.medieninformatik.abgabenmanager.model.Task
import de.medieninformatik.androidapp.R
import org.json.JSONArray

class AddFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.add_fragment, container, false
        )

        val in_name: TextInputEditText = view.findViewById(R.id.input_name)
        val in_description: TextInputEditText = view.findViewById(R.id.input_description)
        val in_date: EditText = view.findViewById(R.id.input_date)
        val in_subject: TextInputEditText = view.findViewById(R.id.input_subject)
        val tasks = listOf<Task>()

        val button_add_task = view.findViewById(R.id.button_createToDos) as Button
        val button_submit = view.findViewById(R.id.button_submit) as Button

        button_add_task.setOnClickListener {
            // TODO: Add tasks to list
        }
        button_submit.setOnClickListener {
            val abgabe = Abgabe(
                getLastId(),
                fillIfEmpty(in_name.text, "Abgabe"),
                fillIfEmpty(in_date.text, "01.01.2021"),
                fillIfEmpty(in_subject.text, "MCI"),
                tasks,
                fillIfEmpty(in_description.text, "Beschreibung"),
                false
            )
            Log.i("Abgabe", abgabe.toString())
            addToJson(abgabe)

        }


        return view
    }

    private fun fillIfEmpty(text: Editable?, s: String): String {
        if (text.toString() == "") {
            return s
        }
        return text.toString()
    }

    private fun getLastId(): Int {
        //TODO: find last id
        return 1
    }

    private fun addToJson(abgabe: Abgabe) {
        val sharedPreferences = context?.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val jsonString = sharedPreferences?.getString("json", "[]")
        val jsonArray = JSONArray(jsonString)

        jsonArray.put(gson.toJson(abgabe))


        sharedPreferences?.edit()?.putString("json", jsonArray.toString())?.apply()
    }

}
