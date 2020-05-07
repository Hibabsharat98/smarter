package com.example.smarter.adapter

import android.content.Context
import android.net.Uri
import androidx.core.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.example.smarter.R
import com.example.smarter.classes.AppInfo
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


class AppsAdapter(private val context1: Context, private val appInfoList: MutableList<AppInfo>) : androidx.recyclerview.widget.RecyclerView.Adapter<AppsAdapter.ViewHolder>() {


    class ViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        var imageView: ImageView = view.findViewById(R.id.imageviewAppManager) //app_icon
        var textViewAppName: TextView = view.findViewById(R.id.Apk_Name) //app_name
        var check : CheckBox =view.findViewById(R.id.appcheck) //check_box


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view2 = LayoutInflater.from(context1).inflate(R.layout.card_view, parent, false)
        return ViewHolder(view2)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.textViewAppName.text = appInfoList[position].appName
        viewHolder.check

        val uri = appInfoList[position].appDrawableURI
        try
        {
            if (uri != Uri.EMPTY)
                Glide.with(context1).load(uri).apply(RequestOptions().error(R.drawable.ic_android)).into(viewHolder.imageView)
            else {
                val img = ContextCompat.getDrawable(context1, R.drawable.ic_android)
                viewHolder.imageView.setImageDrawable(img)
            }
        } catch (e: Exception) {
            val img = ContextCompat.getDrawable(context1, R.drawable.ic_android)
            viewHolder.imageView.setImageDrawable(img)
        }

    }

    override fun getItemCount(): Int {
        return appInfoList.size
    }

    fun updateList(items: List<AppInfo>?) {
        if (items != null) {
            if (items.isNotEmpty()) {
                appInfoList.clear()
                appInfoList.addAll(items)
                notifyDataSetChanged()
            }
        }
    }
}