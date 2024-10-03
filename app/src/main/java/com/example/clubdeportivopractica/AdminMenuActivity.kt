package com.example.clubdeportivopractica

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AdminMenuActivity : AppCompatActivity() {
    // Clase para alojar comportamiento de botones
    class ActionButton<T>(id: Int, activity: Class<T>) {
        var id = id
        var activity = activity
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_menu)

        // Agregar aqui la lista de ids de boton y activity a la que conducen
        val actions = arrayOf(
            ActionButton(R.id.btnSignUp, SignUpActivity::class.java),
            ActionButton(R.id.btnPayment, PaymentActivity::class.java),
            ActionButton(R.id.btnDueDate, DailyExpirationsActivity::class.java),
            ActionButton(R.id.btnGetMembership, GenerateIdCardActivity::class.java),
            ActionButton(R.id.btnActSignUp, SignUpActivitiesActivity::class.java),
            ActionButton(R.id.btnExit, MainActivity::class.java)
        )

        // Inicializar navegaciones
        actions.forEach {
            val ab = it
            val btn = findViewById<Button>(ab.id)
            btn.setOnClickListener {
                val intent = Intent(this, ab.activity)
                startActivity(intent)
            }
        }

    }
}