package com.example.clubdeportivopractica

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PaymentReceiptActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_payment_receipt)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val paymentID = intent.getIntExtra("paymentID", 0)
        val dataBaseHelper = DataBaseHelper(this)
        val payment = dataBaseHelper.getPaymentById(paymentID)
        if (payment == null) {
            Toast.makeText(this, "Id de pago inválido", Toast.LENGTH_SHORT).show()
        } else{
            val clientData = dataBaseHelper.getClientByDni(payment.dni)
            val txtPaymentDate = findViewById<TextView>(R.id.txtPaymentDate)
            txtPaymentDate.text = payment.date
            val txtReceiptNum = findViewById<TextView>(R.id.txtReceiptNumb)
            txtReceiptNum.text = payment.id.toString()
            val txtSurname = findViewById<TextView>(R.id.txtSurname)
            txtSurname.text = clientData.surname
            val txtName = findViewById<TextView>(R.id.txtName)
            txtName.text = clientData.name
            val txtDNI = findViewById<TextView>(R.id.txtDNI)
            txtDNI.text = clientData.dni
            val txtPaymentType = findViewById<TextView>(R.id.txtPaymentType)
            txtPaymentType.text = payment.type
            val txtAmount = findViewById<TextView>(R.id.txtAmountP)
            txtAmount.text = payment.amount.toString()
            if (payment.type == "Cuota") {
                Toast.makeText(this, "Pago de cuota realizado con éxito", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Pago de actividad ${payment.type} realizada con éxito", Toast.LENGTH_LONG).show()
            }

        }



        val backBtn = findViewById<Button>(R.id.btnBackP)
        backBtn.setOnClickListener {
            val intent = Intent(this, AdminMenuActivity::class.java)
            startActivity(intent)
        }
    }
}