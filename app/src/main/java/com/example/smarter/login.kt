package com.example.smarter

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.toast
import org.json.JSONObject

class login : AppCompatActivity() {
    lateinit var E_mail: EditText
   lateinit var Password: EditText
    lateinit var Acontinue: Button
    lateinit var Asignup: Button
    private lateinit var sharedpref: Sharedpref
    override fun onCreate(savedInstanceState: Bundle?) {

        //call up dark mode
        sharedpref = Sharedpref(this)

        if (sharedpref.loadNightmodeState() == true) {
            setTheme(R.style.darkTheme)

        } else
            setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        if (sharedpref.loaduserid() > 0) {
            startActivity(Intent(this, mainscrren::class.java))
            finish()

        } else {

            val jsonobj = JSONObject()
            continues.setOnClickListener {
                val url = "https://smarter.arabyhosting.com/api/user/login.php?username=${email.text}&password=${password.text}"
              //  jsonobj.put("username", email.text)
                //jsonobj.put("password", password.text)
                val que = Volley.newRequestQueue(this@login)
                val req = JsonObjectRequest(
                    Request.Method.GET, url, null,
//                Request.Method.POST,url,jsonobj,

                    Response.Listener {
                        val userid = it.getInt("userid")
                        sharedpref.setuseride(userid)
                        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()

                        startActivity(Intent(this, mainscrren::class.java))


                        // for (i in 0 until jsonArray.length()) {
//                        val userid: JSONObject = jsonArray.getJSONObject(0)
                        // Do you fancy stuff
                        // Example: String gifUrl = jo.getString("url");
                        //  }
//                        println(it)
                        finish()

//                    println("--------------------------------------------")
//                    println(userid.toString())
//                    println("--------------------------------------------")


                    }, Response.ErrorListener { error ->
                        print("the json is :  $error")
                        toast("your Name or password incorrect ")
//                        println(error)

                    })
//                println(Response)

                que.add(req)
            }
        }
        val jsonobj = JSONObject()
        signup.setOnClickListener {
            val url = "https://smarter.arabyhosting.com/api/user/create.php"

            jsonobj.put("username",email.text)
            jsonobj.put("password",password.text)
            val que = Volley.newRequestQueue(this@login)
            val req = JsonObjectRequest(
                Request.Method.POST,url,jsonobj,
//                Request.Method.POST,url,jsonobj,

                Response.Listener {
                    Toast.makeText(this,"Success", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, mainscrren::class.java))
                   // val userid = it.getInt("userid")
                    // for (i in 0 until jsonArray.length()) {
//                        val userid: JSONObject = jsonArray.getJSONObject(0)
                    // Do you fancy stuff
                    // Example: String gifUrl = jo.getString("url");
                    //  }
                  //  sharedpref.setuseride(userid)
                    finish()
//                    println("--------------------------------------------")
//                    println(userid.toString())
//                    println("--------------------------------------------")



                }, Response.ErrorListener {
                        error -> print("the json is :  $error")
                    toast("your Name or password incorrect")



                })
            que.add(req)

        }


                //get to mainscrren //hiba
//            continues.setOnClickListener {
//                startActivity(Intent(this, mainscrren::class.java))
//                finish()
//            }
                // end

                //enable or disable button

                E_mail = findViewById(R.id.email)
                Password = findViewById(R.id.password)
                Acontinue = findViewById(R.id.continues)
                Asignup = findViewById(R.id.signup)
                E_mail.addTextChangedListener(loginTextWatcher)
                password.addTextChangedListener(loginTextWatcher)
            }
            val loginTextWatcher: TextWatcher = object : TextWatcher {
                override fun beforeTextChanged(
                  s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    val usernameInput: String = email.getText().toString().trim({ it <= ' ' })
                    val passwordInput: String = password.getText().toString().trim({ it <= ' ' })
                    continues.setEnabled(!usernameInput.isEmpty() && !passwordInput.isEmpty())
                signup.setEnabled(!usernameInput.isEmpty() && !passwordInput.isEmpty())

            }

                override fun afterTextChanged(s: Editable) {}
            }
            //end


        }


