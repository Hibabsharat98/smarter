package com.example.smarter

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.smarter.R.array.spinner_app_type
import com.example.smarter.adapter.ApkInfoExtractor
import com.example.smarter.adapter.AppsAdapter
import com.example.smarter.classes.AppInfo
import com.example.smarter.classes.AppManager
import com.example.smarter.constant.Constants
import kotlinx.android.synthetic.main.activity_deivemode_disable.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*

class deivemode_disable : AppCompatActivity() {
    private lateinit var sharedpref: Sharedpref
    private var adapter: AppsAdapter? = null

    internal lateinit var appList: MutableList<AppInfo>
    private lateinit var appListAlternate: MutableList<AppInfo>
    internal lateinit var userAppList: MutableList<AppInfo>
    internal lateinit var systemAppList: MutableList<AppInfo>


    private var appManOb: AppManager? = null

    private lateinit var actvityContext: Context

    private var apkInfoExtractor: ApkInfoExtractor? = null

    internal var arrAppType: Array<String>? = null
    private var recyclerViewLayoutManager: androidx.recyclerview.widget.RecyclerView.LayoutManager? = null
    internal var numberOfUserApps: String? = Constants.STRING_EMPTY
    internal var numberOfSystemApps: String? = Constants.STRING_EMPTY
    override fun onCreate(savedInstanceState: Bundle?) {
        sharedpref = Sharedpref(this)
        if (sharedpref.loadNightmodeState() == true) {
            setTheme(R.style.darkTheme)

        }
        else
            setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deivemode_disable)
        appList = ArrayList()
        arrAppType = arrayOf("User Apps", "System Apps")

        appManOb = AppManager()

        userAppList = ArrayList()
        systemAppList = ArrayList()
        appListAlternate = ArrayList()

        actvityContext = this

        val spinnerArrayAdapter = ArrayAdapter.createFromResource(actvityContext, spinner_app_type, R.layout.support_simple_spinner_dropdown_item)
        spinner_App_Type.adapter = spinnerArrayAdapter

        apkInfoExtractor = ApkInfoExtractor(this)
        recyclerViewLayoutManager = androidx.recyclerview.widget.GridLayoutManager(actvityContext, 1)


        getApps(actvityContext as deivemode_disable)

        spinner_App_Type.isSelected = false
        spinner_App_Type.isEnabled = false
    }
    private fun getApps(context: Context) {

        doAsync {
            appManOb = ApkInfoExtractor(context).appManagerInitValues()

            if (appManOb != null) {
                numberOfUserApps = Constants.STRING_EMPTY + appManOb!!.userAppSize
                numberOfSystemApps = Constants.STRING_EMPTY + appManOb!!.systemAppSize

                userAppList.addAll(appManOb!!.userApps)
                systemAppList.addAll(appManOb!!.systemApps)

                appListAlternate.addAll(userAppList)
                appList.addAll(userAppList)

                adapter = AppsAdapter(
                    context,
                    appListAlternate
                )
            } else {

                numberOfUserApps = Constants.STRING_EMPTY + "0"
                numberOfSystemApps = Constants.STRING_EMPTY + "0"

                userAppList.clear()
                systemAppList.clear()
                appListAlternate.clear()
                appList.clear()

                adapter = AppsAdapter(
                    context,
                    appListAlternate
                )
            }

            uiThread {
                recycler_view_Apps.layoutManager = recyclerViewLayoutManager

                if (adapter!!.itemCount > 0) {
                    recycler_view_Apps.adapter = adapter
                    val text = "$numberOfUserApps User apps"
                    app_Counter_App_Manager.text = text

                    spinner_App_Type.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                            val selectedItem = parent.getItemAtPosition(position).toString()

                            if (selectedItem == arrAppType!![0]) {
                                //User Apps
                                val textUser = "$numberOfUserApps User apps"
                                app_Counter_App_Manager.text = textUser
                                appList.clear()
                                appList.addAll(userAppList)
                                adapter?.updateList(userAppList)
                            } else if (selectedItem == arrAppType!![1]) {
                                //System Apps
                                val textSystem = "$numberOfSystemApps System apps"
                                app_Counter_App_Manager.text = textSystem
                                appList.clear()
                                appList.addAll(systemAppList)
                                adapter?.updateList(systemAppList)
                            }
                        } // to close the onItemSelected

                        override fun onNothingSelected(parent: AdapterView<*>) {

                        }
                    }

                    spinner_App_Type.isEnabled = true
                    spinner_App_Type.setSelection(0, true)

                } else {
                    app_Counter_App_Manager.text = getString(R.string.No_Apps)
                    apps_recycler_layoout_ll.visibility = View.GONE
                    recycler_view_Apps.visibility = View.GONE
                    spinner_App_Type.isEnabled = false
                    list_empty_Apps_Appmanager.visibility = View.VISIBLE
                }
            }
        }
    }


}