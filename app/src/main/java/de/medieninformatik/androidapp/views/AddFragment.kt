package de.medieninformatik.androidapp.views

import android.content.Context
import android.os.Bundle
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

class AddFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.add_fragment, container, false)

        val in_name:TextInputEditText = view.findViewById(R.id.input_name)
        val in_description:TextInputEditText = view.findViewById(R.id.input_description)
        val in_date:EditText = view.findViewById(R.id.input_date)
        val in_subject:TextInputEditText = view.findViewById(R.id.input_subject)
        var tasks = listOf<Task>()

        val button_add_task = view.findViewById(R.id.button_createToDos) as Button
        val button_submit = view.findViewById(R.id.button_submit) as Button

        button_add_task.setOnClickListener {
        // TODO: Add tasks to list
        }
        button_submit.setOnClickListener {
            val abgabe:Abgabe = Abgabe(getLastId(), in_name.text.toString(), in_date.text.toString(), in_subject.text.toString(), tasks, in_description.text.toString(), false)
            Log.i("Abgabe", abgabe.toString())
            addToJson(abgabe)

        }


        return view
    }

    private fun getLastId(): Int {
        return 1;
    }

    private fun addToJson(abgabe: Abgabe) {
        val gson = Gson()
        val newJson = gson.toJson(abgabe)
        val sharedPreferences = context?.getSharedPreferences("sharedPrefs", 0)
        val editor = sharedPreferences?.edit()
        var savedJson = sharedPreferences?.getString("json", "")

        savedJson += newJson

        editor?.putString("json", savedJson)
        editor?.apply()
        Log.i("json-content", sharedPreferences?.getString("json", "").toString())
    }
}
