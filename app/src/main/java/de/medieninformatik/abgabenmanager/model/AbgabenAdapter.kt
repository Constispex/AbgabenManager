package de.medieninformatik.abgabenmanager.model

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView
import de.medieninformatik.abgabenmanager.MainActivity
import de.medieninformatik.abgabenmanager.R
import de.medieninformatik.abgabenmanager.views.HomeFragment
import de.medieninformatik.abgabenmanager.views.ShowAbgabeFragment
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

/**
 * Die Klasse dient als Adapter für die ListView in der HomeFragment Klasse.
 */
class AbgabeAdapter(private val context: Context, private val abgaben: List<Abgabe>) :
    BaseAdapter() {

    override fun getCount(): Int {
        return abgaben.size
    }

    override fun getItem(position: Int): Any {
        return abgaben[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    /**
     * Die Methode getView() wird für jedes Listenelement aufgerufen und gibt die View zurück, die für das Listenelement angezeigt werden soll.
     * @param position Position des Listenelements
     * @param convertView View, die für das Listenelement angezeigt werden soll
     * @param parent ViewGroup, die die View enthält
     * @return View, die für das Listenelement angezeigt werden soll
     */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.abgabe_list_item_layout, parent, false)

        val buttonIsDone: CheckBox = view.findViewById(R.id.buttonIsDone)
        val nameTextView = view.findViewById<TextView>(R.id.nameTextView)
        val dateTextView = view.findViewById<TextView>(R.id.dateTextView)

        val abgabe = abgaben[position]

        buttonIsDone.isChecked = abgabe.isDone

        view.setOnClickListener {
            Log.i("Abgabe", "Abgabe with id ${abgabe.id} was selected")
            val showAbgabeFragment = ShowAbgabeFragment.newInstance(abgabe.id)
            val transaction = (context as MainActivity).supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, showAbgabeFragment).commit()
        }


        buttonIsDone.setOnClickListener {
            abgabe.isDone = buttonIsDone.isChecked
            saveToPreferences(abgabe, buttonIsDone.isChecked)

            checkBackgroundColor(view, abgabe.isDone)

            view.callOnClick()
        }

        nameTextView.text = abgabe.name
        dateTextView.text = getDateText(abgabe, dateTextView)

        checkBackgroundColor(view, abgabe.isDone)

        return view
    }

    /**
     * Die Methode speichert die Abgabe in den SharedPreferences.
     */
    private fun saveToPreferences(abgabe: Abgabe, checked: Boolean) {
        val sharedPreferences = context.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val jsonString = sharedPreferences.getString("json", "[]")
        val jsonArray = JSONArray(jsonString)
        val newJsonArray = JSONArray()

        for (i in 0 until jsonArray.length()) {
            val currJsonString = jsonArray.getString(i)
            try {
                val currJsonObj = JSONObject(currJsonString)
                if (currJsonObj.getInt("id") == abgabe.id) {
                    currJsonObj.put("isDone", checked)
                }
                newJsonArray.put(currJsonObj)
            } catch (e: Exception) {
                Log.d("JSON", "Error with JSON: $e")
            }
        }

        val editor = sharedPreferences.edit()
        editor.putString("json", newJsonArray.toString())
        editor.apply()

        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, HomeFragment()).commit()
    }

    /**
     * Die Methode gibt den Text für das Datum zurück, der in der ListView angezeigt werden soll.
     */
    private fun getDateText(abgabe: Abgabe, dateTextView: TextView): String {
        val today = Calendar.getInstance()
        val abgabeDate = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).parse(abgabe.date)
        val difference = (abgabeDate?.time ?: 0) - today.timeInMillis
        val daysUntilAbgabe = TimeUnit.MILLISECONDS.toDays(difference)

        var dateText: String = ""

        if (abgabe.isDone) {
            dateTextView.setTextColor(context.getColor(R.color.colorDateNormal))
            val word = if (daysUntilAbgabe < 0) "war" else "ist"
            dateText = "Abgabe $word am ${abgabe.date}"
            return dateText
        }

        when {
            daysUntilAbgabe < 0 -> {
                dateText = "Abgabe war am ${abgabe.date}"
                dateTextView.setTextColor(context.getColor(R.color.colorDateRed))
            }

            daysUntilAbgabe == 0L -> {
                dateText = "Abgabe ist heute"
                dateTextView.setTextColor(context.getColor(R.color.colorDateYellow))
            }

            daysUntilAbgabe == 1L -> {
                dateText = "Abgabe ist morgen"
                dateTextView.setTextColor(context.getColor(R.color.colorDateYellow))
            }

            daysUntilAbgabe < 7 -> {
                dateText = "Abgabe ist in $daysUntilAbgabe Tagen"
                dateTextView.setTextColor(context.getColor(R.color.colorDateYellow))
            }

            daysUntilAbgabe < 14 -> {
                dateText = "Abgabe ist am ${abgabe.date}"
                dateTextView.setTextColor(context.getColor(R.color.colorDateNormal))
            }
        }

        return dateText
    }

    /**
     * Die Methode ändert die Hintergrundfarbe der View, je nachdem ob die Abgabe erledigt ist oder nicht.
     */
    private fun checkBackgroundColor(view: View, isDone: Boolean) {
        val layout = view.findViewById<ViewGroup>(R.id.abgabe_list_item_layout)
        if (isDone) {
            layout.background = context.getDrawable(R.color.colorDone)
        } else {
            layout.background = context.getDrawable(R.color.colorItemBackground)
        }
    }
}

