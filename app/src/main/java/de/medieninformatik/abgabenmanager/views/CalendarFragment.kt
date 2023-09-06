package de.medieninformatik.abgabenmanager.views

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.github.sundeepk.compactcalendarview.domain.Event
import de.medieninformatik.abgabenmanager.R
import de.medieninformatik.abgabenmanager.model.Abgabe
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Die Klasse dient als Fragment für die Kalender Seite.
 */
class CalendarFragment : Fragment() {
    /**
     * Die Methode onCreateView() wird aufgerufen, wenn das Fragment erstellt wird.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.calendar_fragment, container, false)

        updateCalendar(view)

        return view
    }

    /**
     * Die Methode updateCalendar() wird aufgerufen, wenn der Kalender aktualisiert werden soll.
     */
    private fun updateCalendar(view: View?) {
        val calendarView: CompactCalendarView? = view?.findViewById(R.id.calendarView)
        val abgaben = getAbgaben()
        val calendarEvents = abgabenToCalendar(abgaben)
        val currDate = Calendar.getInstance()
        val endDate = Calendar.getInstance()
        endDate.add(Calendar.MONTH, 6)

        calendarView?.setFirstDayOfWeek(Calendar.MONDAY)

        for (calendarEvent in calendarEvents) {
            calendarView?.addEvent(Event(Color.BLUE, calendarEvent.timeInMillis))
            calendarView?.setListener(object : CompactCalendarView.CompactCalendarViewListener {
                override fun onDayClick(dateClicked: Date?) {
                    Log.i("Calendar", "Day was clicked: $dateClicked")
                    val selectedDate = dateClicked?.let {
                        SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(
                            it
                        )
                    }

                    val abgabenFilter = abgaben.filter { it.date == selectedDate }

                    // Remove fragment if it exists
                    val fragmentManager = requireActivity().supportFragmentManager
                    val fragments = fragmentManager.fragments
                    val transaction = fragmentManager.beginTransaction()

                    Log.i("Calendar", "Fragments: $fragments")
                    for (fragment in fragments) {
                        if (fragment is ShowAbgabeFragment) {
                            transaction.remove(fragment)
                        }
                    }
                    transaction.commit()

                    if (abgabenFilter.isNotEmpty()) {
                        for (abgabe in abgabenFilter) {
                            val showAbgabeFragment = ShowAbgabeFragment.newInstance(abgabe.id)
                            val addItemsTransaction =
                                requireActivity().supportFragmentManager.beginTransaction()
                            addItemsTransaction.add(R.id.calendarItemLayout, showAbgabeFragment)
                            addItemsTransaction.addToBackStack(null)
                            addItemsTransaction.commit()
                        }
                    }
                }

                override fun onMonthScroll(firstDayOfNewMonth: Date?) {
                    Log.i("Calendar", "Month was scrolled to: $firstDayOfNewMonth")
                }
            })
        }
    }

    /**
     * Die Methode abgabenToCalendar() wandelt eine Liste von Abgaben in eine Liste von Calendar Objekten um.
     */
    private fun abgabenToCalendar(list: List<Abgabe>): MutableList<Calendar> {
        val calendar: Calendar = Calendar.getInstance()
        val calendarEvents = mutableListOf<Calendar>()
        for (abgabe in list) {
            Log.i("Abgabe", "Abgabe: ${abgabe.name}, Date: ${abgabe.date}")
            calendar.time = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).parse(abgabe.date)!!
            calendarEvents.add(calendar.clone() as Calendar)
        }

        return calendarEvents
    }

    /**
     * Die Methode getAbgaben() gibt die Liste der gespeicherten Abgaben zurück.
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
