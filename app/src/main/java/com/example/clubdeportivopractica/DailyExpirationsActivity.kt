package com.example.clubdeportivopractica

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DailyExpirationsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_daily_expirations)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val dataBaseHelper = DataBaseHelper(this)
        val payments = dataBaseHelper.getExpiredPayments()

        if (payments.isEmpty()) {
            Toast.makeText(this, "No hay pagos vencidos", Toast.LENGTH_SHORT).show()
        } else{
            val table = findViewById<TableLayout>(R.id.tableLayout)
            table.removeAllViews()
            val inflater = LayoutInflater.from(this)
            val headerRow = inflater.inflate(R.layout.expied_payment_table_row, null) as TableRow
            table.addView(headerRow, 0)
            payments.forEach {
                val row = inflater.inflate(R.layout.expied_payment_table_row, null) as TableRow
                row.findViewById<TextView>(R.id.clientDni).text = it.dni
                row.findViewById<TextView>(R.id.paymentExpirationDate).text = it.date
                row.findViewById<TextView>(R.id.paymentAmount).text = it.amount.toString()
                table.addView(row, table.childCount)
            }
        }

        val backBtn = findViewById<Button>(R.id.btnBack)
        backBtn.setOnClickListener {
            val intent = Intent(this, AdminMenuActivity::class.java)
            startActivity(intent)
        }
    }
}