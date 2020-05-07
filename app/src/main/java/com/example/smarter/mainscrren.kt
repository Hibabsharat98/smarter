package com.example.smarter

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_mainscrren.*

class mainscrren : AppCompatActivity() {
    private lateinit var sharedpref: Sharedpref
    override fun onCreate(savedInstanceState: Bundle?) {
        sharedpref = Sharedpref(this)
        if (sharedpref.loadNightmodeState() == true) {
            setTheme(R.style.darkTheme)

        }
        else
            setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainscrren)

        to_phonemode.setOnClickListener {
            startActivity(Intent(this, phonemode::class.java))
            finish()
        }

        to_phonemode2.setOnClickListener {
            startActivity(Intent(this, phonemode::class.java))
            finish()
        }

        to_drivemode.setOnClickListener {
            startActivity(Intent(this, drivemode::class.java))
            finish()}

        to_drivemode2.setOnClickListener {
            startActivity(Intent(this, drivemode::class.java))
            finish()
        }

        to_note1.setOnClickListener {
            finish()
            startActivity(Intent(this, note::class.java)) }

        to_note2.setOnClickListener {
            startActivity(Intent(this, note::class.java))
            finish()
        }

        to_timeline1.setOnClickListener {
            startActivity(Intent(this, timeline::class.java))
            finish()
        }

        to_timeline2.setOnClickListener {
            startActivity(Intent(this, timeline::class.java))
            finish()
        }

        to_setting1.setOnClickListener {
            startActivity(Intent(this, setting::class.java))
            finish()
        }

        to_setting2.setOnClickListener {
            startActivity(Intent(this, setting::class.java))
            finish()
        }
    }
    override fun onBackPressed() {
        // build alert dialog
        val dialogBuilder = AlertDialog.Builder(this)

        // set message of alert dialog
        dialogBuilder.setMessage("Do you want to close Smarter ?")
            // if the dialog is cancelable
            .setCancelable(false)
            // positive button text and action
            .setPositiveButton("Yes", DialogInterface.OnClickListener {
                    dialog, id -> finish()
                finish()
            })
            // negative button text and action
            .setNegativeButton("No", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
            })

        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle("Close")
        // show alert dialog
        alert.show()
    }
}



