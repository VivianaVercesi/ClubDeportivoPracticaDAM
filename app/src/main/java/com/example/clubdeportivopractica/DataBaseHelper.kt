package com.example.clubdeportivopractica

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DataBaseHelper (context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        // General DB config
        private const val DATABASE_NAME = "ClubDeportivo"
        private const val DATABASE_VERSION = 5

        // User table
        private const val TABLE_USER = "User"
        private const val USER_COLUMN_ID = "id"
        private const val USER_COLUMN_USERNAME = "username"
        private const val USER_COLUMN_PASSWORD = "password"

        // Client table
        private const val TABLE_CLIENT = "Client"
        private const val CLIENT_COLUMN_DNI = "dni"
        private const val CLIENT_COLUMN_NAME = "name"
        private const val CLIENT_COLUMN_SURNAME = "surname"
        private const val CLIENT_COLUMN_MEMBER = "member"
        private const val CLIENT_COLUMN_FIT = "fit"

        // Payment table
        private const val TABLE_PAYMENT = "Payment"
        private const val PAYMENT_COLUMN_ID = "id"
        private const val PAYMENT_COLUMN_DNI = "dni"
        private const val PAYMENT_COLUMN_AMOUNT = "amount"
        private const val PAYMENT_COLUMN_DATE = "date"
        private const val PAYMENT_COLUMN_TYPE = "type"

        // Init values
        var loggedUser: User? = null
        fun logOut(){
            loggedUser = null
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {

        val createTableUser = "CREATE TABLE $TABLE_USER ($USER_COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $USER_COLUMN_USERNAME TEXT, $USER_COLUMN_PASSWORD TEXT)"
        db?.execSQL(createTableUser)
        db?.execSQL("INSERT INTO $TABLE_USER ($USER_COLUMN_USERNAME, $USER_COLUMN_PASSWORD) VALUES ('admin', 'admin')")

        val createTableClient = "CREATE TABLE $TABLE_CLIENT ($CLIENT_COLUMN_DNI TEXT PRIMARY KEY, $CLIENT_COLUMN_NAME TEXT, $CLIENT_COLUMN_SURNAME TEXT, $CLIENT_COLUMN_MEMBER BOOLEAN, $CLIENT_COLUMN_FIT BOOLEAN)"
        db?.execSQL(createTableClient)

        //Registered clients for tests
        val clientValues = ContentValues()
        clientValues.put(CLIENT_COLUMN_DNI, "123")
        clientValues.put(CLIENT_COLUMN_NAME, "Javier")
        clientValues.put(CLIENT_COLUMN_SURNAME, "Rodriguez")
        clientValues.put(CLIENT_COLUMN_MEMBER, true)
        clientValues.put(CLIENT_COLUMN_FIT, true)
        db?.insert(TABLE_CLIENT, null, clientValues)


        val createTablePayment = "CREATE TABLE $TABLE_PAYMENT ($PAYMENT_COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $PAYMENT_COLUMN_DNI TEXT, $PAYMENT_COLUMN_AMOUNT INTEGER, $PAYMENT_COLUMN_TYPE TEXT, $PAYMENT_COLUMN_DATE TEXT)"
        db?.execSQL(createTablePayment)
        //Expired payments for tests
        val paymentValues = ContentValues()
        paymentValues.put(PAYMENT_COLUMN_DNI, "123")
        paymentValues.put(PAYMENT_COLUMN_AMOUNT, "444")

        paymentValues.put(PAYMENT_COLUMN_DATE, "2024-05-01")
        paymentValues.put(PAYMENT_COLUMN_TYPE, "Cuota")
        db?.insert(TABLE_PAYMENT, null, paymentValues)


    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USER")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_CLIENT")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_PAYMENT")
        onCreate(db)
    }

    fun logIn(username: String, password: String): Boolean{
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USER WHERE $USER_COLUMN_USERNAME = '$username' AND $USER_COLUMN_PASSWORD = '$password'"
        val cursor = db.rawQuery(query, null)
        if (cursor.count <= 0) {
            cursor.close()
            return false
        }
        cursor.moveToFirst()
        loggedUser = User(cursor.getInt(0), username)
        cursor.close()
        return true
    }

    fun signUpClient(dni: String, name: String, surname: String, member: Boolean, fit: Boolean): Error?{
        val db = this.writableDatabase
        val query = "SELECT * FROM $TABLE_CLIENT WHERE $CLIENT_COLUMN_DNI = '$dni'"
        val cursor = db.rawQuery(query, null)
        if (cursor.count > 0) {
            cursor.close()
            return Error("DNI already exists")
        } else {
            val values = ContentValues()
            values.put(CLIENT_COLUMN_DNI, dni)
            values.put(CLIENT_COLUMN_NAME, name)
            values.put(CLIENT_COLUMN_SURNAME, surname)
            values.put(CLIENT_COLUMN_MEMBER, member)
            values.put(CLIENT_COLUMN_FIT, fit)
            db.insert(TABLE_CLIENT, null, values)
            cursor.close()
            return null
        }
    }

    fun getClientByDni(dni: String): Client{
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_CLIENT WHERE $CLIENT_COLUMN_DNI = ? LIMIT 1"
        val cursor = db.rawQuery(query, arrayOf(dni))
        if (cursor.count != 1) {
            cursor.close()
            throw Exception("Client not found")
        } else {
            cursor.moveToFirst()
            val client = Client(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3) == 1, cursor.getInt(4)==1)
            cursor.close()
            return client
        }
    }

    fun newPayment(dni: String, amount: Int, type: String): Int{
        val db = this.writableDatabase
        val query = "SELECT * FROM $TABLE_CLIENT WHERE $CLIENT_COLUMN_DNI = '$dni'"
        val cursor = db.rawQuery(query, null)
        if (cursor.count <= 0) {
            cursor.close()
            throw Error("No se encontró al cliente")

        } else {
            cursor.moveToFirst()
            val client = Client(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3) == 1, cursor.getInt(4)==1)
            cursor.close()
            if (!client.member && type == "Cuota") {
                throw Error("Solo los socios pueden pagar con cuota")
            }
            if (client.member && type != "Cuota") {
                throw Error("Los socios no necesitan pagar por actividades")
            } else {
                val values = ContentValues()
                values.put(PAYMENT_COLUMN_DNI, dni)
                values.put(PAYMENT_COLUMN_AMOUNT, amount)
                val date = LocalDate.now()
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val formattedDate = date.format(formatter)
                values.put(PAYMENT_COLUMN_DATE, formattedDate)
                values.put(PAYMENT_COLUMN_TYPE, type)
                val paymentID = db.insert(TABLE_PAYMENT, null, values)
                cursor.close()
                return paymentID.toInt()
            }

        }

    }

    fun getPaymentById(paymentID: Int): Payment?{
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_PAYMENT WHERE $PAYMENT_COLUMN_ID = '$paymentID'"
        val cursor = db.rawQuery(query, null)
        if (cursor.count <= 0) {
            cursor.close()
            return null
        } else{
            cursor.moveToFirst()
            val payment = Payment(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getString(3), cursor.getString(4))
            cursor.close()
            return payment
        }
    }

    fun getExpiredPayments(): List<Payment> {
        val payments = mutableListOf<Payment>()
        val db = this.readableDatabase
        val query = "SELECT * \n" +
                "FROM $TABLE_PAYMENT \n" +
                "WHERE \n" +
                "    $PAYMENT_COLUMN_DATE < DATE('now', '-1 month')" +
                "    AND $PAYMENT_COLUMN_TYPE = 'Cuota'" +
                "    AND $PAYMENT_COLUMN_DNI NOT IN (" +
                "        SELECT $PAYMENT_COLUMN_DNI " +
                "        FROM $TABLE_PAYMENT " +
                "        WHERE $PAYMENT_COLUMN_DATE >= DATE('now', '-1 month')" +
                "    )"
        val cursor = db.rawQuery(query, null)
        if (cursor.count <= 0) {
            cursor.close()
        } else {
            cursor.moveToFirst()
            do {
                val payment = Payment(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getString(3), cursor.getString(4))
                payments.add(payment)
            } while (cursor.moveToNext())
            cursor.close()
        }
        return payments
    }

}