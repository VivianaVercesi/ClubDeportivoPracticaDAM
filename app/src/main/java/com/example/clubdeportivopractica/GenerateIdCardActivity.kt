package com.example.clubdeportivopractica

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class GenerateIdCardActivity : AppCompatActivity() {
    private lateinit var dataBaseHelper: DataBaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_generate_id_card)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val generateBtn = findViewById<Button>(R.id.btnGenerate)
        val inputTxt = findViewById<TextView>(R.id.txtClientDNI)
        generateBtn.setOnClickListener {
            val dni = inputTxt.text.toString();
            if (dni.isBlank()) {
                Toast.makeText(this, "Ingrese DNI del cliente", Toast.LENGTH_SHORT).show()
            } else {
                dataBaseHelper = DataBaseHelper(this);
                try {
                    val client = dataBaseHelper.getClientByDni(dni)
                    val intent = Intent(this, IdCardActivity::class.java)
                    intent.putExtra("name", client.surname + ", " + client.name)
                    intent.putExtra("dni", client.dni)
                    intent.putExtra("member", client.member)
                    startActivity(intent)
                } catch(e: Exception) {
                    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        val backBtn = findViewById<Button>(R.id.btnBack)
        backBtn.setOnClickListener {
            val intent = Intent(this, AdminMenuActivity::class.java)
            startActivity(intent)
        }
    }
}