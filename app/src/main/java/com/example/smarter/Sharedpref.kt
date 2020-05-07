package com.example.smarter

import android.content.Context
import android.content.SharedPreferences

class Sharedpref(context: Context) {
    internal var mySharedpref:SharedPreferences
    init {
        mySharedpref=context.getSharedPreferences("file name",Context.MODE_PRIVATE)

    }
    fun logout()
    {
        val editor=mySharedpref.edit()
        editor.clear();
        editor.commit();
    }
    fun setNightmodeState(state:Boolean?)
    {
        val editor=mySharedpref.edit()
        editor.putBoolean("night mode",state!!)
        editor.commit()
    }
    fun setuseride(state:Int)
    {
        val editor=mySharedpref.edit()
        editor.putInt("UserID",state)

        editor.commit()

    }
    fun loaduserid():Int{
        return mySharedpref.getInt("UserID",0)
    }
    fun loadNightmodeState():Boolean{
        return mySharedpref.getBoolean("night mode",false)
    }

}

