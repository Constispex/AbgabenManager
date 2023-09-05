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
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import de.medieninformatik.abgabenmanager.model.Abgabe
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

        val inName: TextInputEditText = view.findViewById(R.id.input_name)
        val inDescription: TextInputEditText = view.findViewById(R.id.input_description)
        val inDate: EditText = view.findViewById(R.id.input_date)

        val buttonCancel = view.findViewById(R.id.button_cancel) as Button
        val buttonSubmit = view.findViewById(R.id.button_submit) as Button
        var isDate = false

        inDate.addTextChangedListener(
            onTextChanged = { text, _, _, _ ->
                if (text.toString().length == 2) {
                    inDate.setText(text.toString() + ".")
                    inDate.setSelection(inDate.text.toString().length)
                }
                if (text.toString().length == 5) {
                    inDate.setText(text.toString() + ".")
                    inDate.setSelection(inDate.text.toString().length)
                }
                isDate = text.toString().matches(Regex("\\d{2}\\.\\d{2}\\.\\d{4}"))
            }

        )

        buttonCancel.setOnClickListener {
            val homeFragment = HomeFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frameLayout, homeFragment)
            transaction.commit()
        }

        buttonSubmit.setOnClickListener {

            val abgabe = Abgabe(
                getLastId(),
                fillIfEmpty(inName.text, "Abgabe " + getLastId().toString()),
                fillIfEmpty(inDate.text, "01.01.2021"),
                fillIfEmpty(inDescription.text, "Beschreibung"),
                false
            )
            if (isDate) {
                Log.i("Abgabe", abgabe.toString())
                addToJson(abgabe)

                buttonCancel.performClick()
            } else {
                Log.i("Abgabe", "Date is not valid")
                inDate.error = "Date is not valid"
            }
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
        val sharedPreferences = context?.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val jsonString = sharedPreferences?.getString("json", "[]")
        val jsonArray = JSONArray(jsonString)
        return jsonArray.length() + 1
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
