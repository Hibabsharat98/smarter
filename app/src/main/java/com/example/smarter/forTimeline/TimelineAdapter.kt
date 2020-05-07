package com.example.smarter.forTimeline

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smarter.R
import com.example.smarter.timeline_cerate

class TimelineAdapter (private val context: Context,
                       private val dataList: ArrayList<HashMap<String, String>>) : BaseAdapter(){

    private val inflater: LayoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    override fun getCount(): Int { return dataList.size }
    override fun getItem(position: Int): Int { return position }
    override fun getItemId(position: Int): Long { return position.toLong() }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var dataitem = dataList[position]

        val rowView = inflater.inflate(R.layout.timeline_list, parent, false)
        rowView.findViewById<TextView>(R.id.date).text = dataitem.get("date")
        rowView.findViewById<TextView>(R.id.time).text = dataitem.get("time")
        rowView.findViewById<TextView>(R.id.emoji).text = dataitem.get("emoji")
        rowView.findViewById<TextView>(R.id.desc).text = dataitem.get("text")



        rowView.tag = position
        return rowView
    }
}