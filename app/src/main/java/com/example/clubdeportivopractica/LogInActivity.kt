package com.example.clubdeportivopractica

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class LogInActivity : AppCompatActivity() {
    private lateinit var dataBaseHelper: DataBaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        DataBaseHelper.logOut()
        val button = findViewById<Button>(R.id.btnLogIn)
        button.setOnClickListener{
            val username = findViewById<EditText>(R.id.txtUser)
            val password = findViewById<EditText>(R.id.txtPass)
            dataBaseHelper = DataBaseHelper(this)
            if (dataBaseHelper.logIn(username.text.toString(), password.text.toString())){
                val intent = Intent(this, AdminMenuActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
            }

        }

    }
}
