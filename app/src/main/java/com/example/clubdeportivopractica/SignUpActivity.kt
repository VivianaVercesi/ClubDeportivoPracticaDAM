package com.example.clubdeportivopractica

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity



class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)
        val button = findViewById<Button>(R.id.btnClientSignUp)
        button.setOnClickListener{
            //check if text fields are empty
            val nameEditText = findViewById<EditText>(R.id.txtClientName)
            val surnameEditText = findViewById<EditText>(R.id.txtClientSurname)
            val dniEditText = findViewById<EditText>(R.id.txtClientDni)
            val memberCheckBox = findViewById<CheckBox>(R.id.chbMember)
            val fitCheckBox = findViewById<CheckBox>(R.id.chbFit)
            if (nameEditText.text.isEmpty() || surnameEditText.text.isEmpty() || dniEditText.text.isEmpty()) {
                Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                if (!fitCheckBox.isChecked) {
                    Toast.makeText(this, "Nuevos clientes deben tener apto médico", Toast.LENGTH_SHORT).show()
                } else {
                    //SignUp logic
                    val name = nameEditText.text.toString()
                    val surname = surnameEditText.text.toString()
                    val dni = dniEditText.text.toString()
                    val member = memberCheckBox.isChecked
                    val db = DataBaseHelper(this)
                    val error:Error? = db.signUpClient(dni, name, surname, member, true)
                    if (error != null) {
                        Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                        nameEditText.text.clear()
                        surnameEditText.text.clear()
                        dniEditText.text.clear()
                        memberCheckBox.isChecked = false
                        fitCheckBox.isChecked = false

                        if (member) {
                            val intent = Intent(this, PaymentActivity::class.java)
                            intent.putExtra("dni", dni)
                            intent.putExtra("paymentType", "Cuota")
                            startActivity(intent)
                        }

                    }
                }
            }

        }

        val btnBack = findViewById<Button>(R.id.btnBackAdm)
        btnBack.setOnClickListener{
            val intent = Intent(this, AdminMenuActivity::class.java)
            startActivity(intent)
        }
    }
}