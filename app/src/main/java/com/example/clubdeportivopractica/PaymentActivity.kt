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
import kotlin.properties.Delegates
import kotlin.reflect.typeOf



var paymentType: String? = null


class PaymentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_payment)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val dniFromIntent = intent.getStringExtra("dni")

        if (dniFromIntent != null) {
            val dniEditText = findViewById<EditText>(R.id.txtClientDNI)
            dniEditText.setText(dniFromIntent.toString())
        }

        paymentType = intent.getStringExtra("paymentType")
        if(paymentType == null){
            paymentType = "Cuota"
        }

        val btnPayment: Button = findViewById(R.id.btnPay)
        btnPayment.setOnClickListener {
            val amountEditText = findViewById<EditText>(R.id.txtAmount)
            val amount = amountEditText.text.toString().toIntOrNull()
            val dniEditText = findViewById<EditText>(R.id.txtClientDNI)
            val dni = dniEditText.text.toString()
            if (amount == null || amount <= 0) {
                Toast.makeText(this, "Por favor ingrese un monto válido", Toast.LENGTH_SHORT).show()
            } else {
                if(dni.isEmpty()){
                    Toast.makeText(this, "Por favor ingrese un DNI válido", Toast.LENGTH_SHORT).show()
                } else{
                    val dataBaseHelper = DataBaseHelper(this)
                    try {
                        val paymentID = dataBaseHelper.newPayment(dni, amount, paymentType.toString())
                        val intent = Intent(this, PaymentReceiptActivity::class.java)
                        intent.putExtra("paymentID", paymentID)
                        startActivity(intent)
                    } catch (e: Error){
                        Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                    }

                }
            }

        }

        val backBtn = findViewById<Button>(R.id.btnBackAM)
        backBtn.setOnClickListener {
            val intent = Intent(this, AdminMenuActivity::class.java)
            startActivity(intent)
        }

    }
}