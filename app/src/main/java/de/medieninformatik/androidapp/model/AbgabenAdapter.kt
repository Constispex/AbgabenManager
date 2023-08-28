package de.medieninformatik.androidapp.model

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import de.medieninformatik.abgabenmanager.model.Abgabe
import de.medieninformatik.androidapp.R

class AbgabeAdapter(private val context: Context, private val abgaben: List<Abgabe>) : BaseAdapter() {

    override fun getCount(): Int {
        return abgaben.size
    }

    override fun getItem(position: Int): Any {
        return abgaben[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.abgabe_list_item_layout, parent, false)

        val nameTextView = view.findViewById<TextView>(R.id.nameTextView)
        val dateTextView = view.findViewById<TextView>(R.id.dateTextView)

        val abgabe = abgaben[position]
        nameTextView.text = abgabe.name
        dateTextView.text = abgabe.date

        return view
    }
}
