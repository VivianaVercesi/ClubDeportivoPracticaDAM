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

class IdCardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_id_card)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val member = findViewById<TextView>(R.id.title_socio)
        val name = findViewById<TextView>(R.id.name)
        val dni = findViewById<TextView>(R.id.dni)

        name.text = intent.getStringExtra("name")
        dni.text = "DNI: " + intent.getStringExtra("dni")
        member.text = if(intent.getBooleanExtra("member", false)) "Socio" else "No Socio"

        val printBtn = findViewById<Button>(R.id.btnPrint)
        printBtn.setOnClickListener {
            Toast.makeText(this, "Imprimiendo carnet", Toast.LENGTH_SHORT).show()
        }
        val backBtn = findViewById<Button>(R.id.btnBackAdminM)
        backBtn.setOnClickListener {
            val intent = Intent(this, GenerateIdCardActivity::class.java)
            startActivity(intent)
        }
    }
}