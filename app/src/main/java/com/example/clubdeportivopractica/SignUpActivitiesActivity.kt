package com.example.clubdeportivopractica

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SignUpActivitiesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up_activities)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val confBtn = findViewById<Button>(R.id.btnActivitySignUp)
        confBtn.setOnClickListener {
            //SignUp logic
            val activityEditText = findViewById<EditText>(R.id.txtActivity)
            val activity = activityEditText.text.toString()
            val dniEditText = findViewById<EditText>(R.id.txtClientDni)
            val dni = dniEditText.text.toString()
            if (activity.isEmpty()) {
                Toast.makeText(this, "Por favor complete el campo de actividad", Toast.LENGTH_SHORT)
                    .show()
            } else {
                if (dni.isEmpty()) {
                    Toast.makeText(this, "Por favor complete el campo de DNI", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    val dataBaseHelper = DataBaseHelper(this)
                    var client: Client? = null
                    try {
                        client = dataBaseHelper.getClientByDni(dni)
                    } catch (e: Exception) {
                        Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                    }
                    if (client?.member == false) {
                        val intent = Intent(this, PaymentActivity::class.java)
                        intent.putExtra("dni", client?.dni)
                        intent.putExtra("paymentType", activity)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Los socios no necesitan pagar por actividades", Toast.LENGTH_SHORT)
                            .show()
                    }

                }

            }
            val backBtn = findViewById<Button>(R.id.btnBackAdmM)
            backBtn.setOnClickListener {
                val intent = Intent(this, AdminMenuActivity::class.java)
                startActivity(intent)
            }
        }
    }
}