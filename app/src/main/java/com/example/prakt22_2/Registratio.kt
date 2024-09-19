package com.example.prakt22_2

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.android.material.snackbar.Snackbar

class Registratio : AppCompatActivity() {
    private lateinit var pref: SharedPreferences
    private lateinit var login: EditText
    private lateinit var password: EditText
    private lateinit var signin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registratio)
        login = findViewById(R.id.login)
        password = findViewById(R.id.password)
        signin = findViewById(R.id.registration)

        signin.setOnClickListener {
            if (login.text.isNotEmpty() && password.text.isNotEmpty()) {
                pref = getSharedPreferences("Register", MODE_PRIVATE)
                val ed = pref.edit()
                ed.putString("login", login.text.toString())
                ed.putString("password", password.text.toString())
                ed.apply()

                val userLogin = login.text.toString()
                val dbName = "${userLogin}_database"

                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("DB_NAME", dbName)
                startActivity(intent)
            }
            else{
                showSnackbar("Обнаружены пустые поля")
            }
        }
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show()
    }
}