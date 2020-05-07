package com.example.smarter

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_setting.*


class setting : AppCompatActivity() {
    private lateinit var sharedpref: Sharedpref
    override fun onCreate(savedInstanceState: Bundle?) {
        sharedpref = Sharedpref(this)
        if (sharedpref.loadNightmodeState() == true) {
            setTheme(R.style.darkTheme)

        }
        else
            setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        mode.setOnClickListener {
            startActivity(Intent(this, setting_mode::class.java))
            finish()
        }

        notification.setOnClickListener {
            val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER)
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION)
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Smarter Notifications Tone")
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, null as Uri?)
            this.startActivityForResult(intent, 5)
         /*   fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent) {
                if (resultCode == Activity.RESULT_OK && requestCode == 5) {
                    val uri =
                        intent.getParcelableExtra<Uri>(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
                    if (uri != null) {
                        this.chosenRingtone = uri.toString()
                    } else {
                        this.chosenRingtone = null
                    }
                }
            }*/

            }

        log.setOnClickListener {
            // build alert dialog
            val dialogBuilder = AlertDialog.Builder(this)

            // set message of alert dialog
            dialogBuilder.setMessage("Do you want to logout ?")
                // if the dialog is cancelable
                .setCancelable(false)
                // positive button text and action
                .setPositiveButton("Yes", DialogInterface.OnClickListener {
                        dialog, id -> /////////////
                    sharedpref = Sharedpref(context = this)
                    sharedpref.logout()
                    startActivity(Intent(this, login::class.java))
                    finish()
                })
                // negative button text and action
                .setNegativeButton("No", DialogInterface.OnClickListener {
                        dialog, id -> dialog.cancel()
                })

            // create dialog box
            val alert = dialogBuilder.create()
            // set title for alert dialog box
            alert.setTitle("Logout")
            // show alert dialog
            alert.show()
        }

    }
    override fun onBackPressed() {
        startActivity(Intent(this,mainscrren::class.java))
        finish()
    }

}

