package com.example.smarter.classes
import com.example.smarter.classes.AppInfo
import java.util.ArrayList

class AppManager {
    var userApps: MutableList<AppInfo> = ArrayList()
    var systemApps: MutableList<AppInfo> = ArrayList()
    var userAppSize: Int = 0
    var systemAppSize: Int = 0

}
