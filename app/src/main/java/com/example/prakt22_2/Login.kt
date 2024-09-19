package com.example.prakt22_2

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar

class Login : AppCompatActivity() {
    private lateinit var pref: SharedPreferences
    private lateinit var login: EditText
    private lateinit var password: EditText
    private lateinit var signin: Button
    private lateinit var regist: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        login = findViewById(R.id.login)
        password = findViewById(R.id.password)
        signin = findViewById(R.id.registration)
        regist = findViewById(R.id.register_click)

        regist.setOnClickListener {
            var intent = Intent(this, Registratio::class.java)
            startActivity(intent)
        }

        signin.setOnClickListener {
            if (login.text.isNotEmpty() && password.text.isNotEmpty()) {
                pref = getSharedPreferences("Register", MODE_PRIVATE)

                val storedLogin = pref.getString("login", "Default")
                val storedPassword = pref.getString("password", "Default")

                if (login.text.toString() != storedLogin || password.text.toString() != storedPassword) {
                    showSnackbar("Неверный логин или пароль")
                } else {
                    val userLogin = login.text.toString()
                    val dbName = "${userLogin}_database"
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("DB_NAME", dbName)
                    startActivity(intent)
                }
            }
            else {
                showSnackbar("Обнаружены пустые поля")
            }
        }
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show()
    }
}
