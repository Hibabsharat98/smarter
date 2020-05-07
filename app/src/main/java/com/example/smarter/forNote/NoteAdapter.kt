package com.example.smarter.forNote

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smarter.R
import com.example.smarter.note_cerate
import com.squareup.picasso.Picasso

class NoteAdapter(private val context: Context,
                  private val dataList: ArrayList<HashMap<String, String>>) : BaseAdapter() {
    private val inflater: LayoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    override fun getCount(): Int { return dataList.size }
    override fun getItem(position: Int): Int { return position }
    override fun getItemId(position: Int): Long { return position.toLong() }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var dataitem = dataList[position]

        val rowView = inflater.inflate(R.layout.note_list, parent, false)
        rowView.findViewById<TextView>(R.id.for_text).text = dataitem.get("text")


        Picasso.get()
            .load(dataitem.get("Images"))
            .resize(50, 50)
            .centerCrop()
            .into(rowView.findViewById<ImageView>(R.id.row_image))


        rowView.tag = position
        return rowView
    }
}
