package com.example.sqlitebasedata

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class BaseDataActivity : AppCompatActivity() {

    private val db = DBHelper(this, null)
    lateinit var role: String
    private lateinit var toolbarMain: Toolbar
    private lateinit var nameET: EditText
    private lateinit var ageET: EditText
    private lateinit var roleSpinnerS: Spinner
    private lateinit var saveBTN: Button
    private lateinit var loadBTN: Button
    private lateinit var clearBTN: Button
    private lateinit var nameTV: TextView
    private lateinit var ageTV: TextView
    private lateinit var roleTV: TextView
    private var roles = mutableListOf(
        "Должность",
        "Инженер",
        "Программист",
        "Оператор",
        "Слесарь",
        "Электрик",
        "Технолог",
        "ОТК",
        "Мастер",
        "Токарь",
        "Фрезеровщик",
        "Шлифовщик",
        "Термист"
    )

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_base_data)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        init()

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            roles
        )


        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        roleSpinnerS.adapter = adapter
        val itemSelectedListener: AdapterView.OnItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val item = parent?.getItemAtPosition(position) as String
                    role = item
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        roleSpinnerS.onItemSelectedListener = itemSelectedListener

        saveBTN.setOnClickListener {
            if (nameET.text.isEmpty() ||
                ageET.text.isEmpty() ||
                role == "Должность") return@setOnClickListener

            val name = nameET.text.toString()
            val age = ageET.text.toString()

            db.addName(name, age, role)
            Toast.makeText(this, "$name добавлен в базу данных", Toast.LENGTH_SHORT).show()

            nameET.text.clear()
            ageET.text.clear()
            roleSpinnerS.setSelection(0)
        }

        loadBTN.setOnClickListener {
            nameTV.text = ""
            ageTV.text = ""
            roleTV.text = ""
            val cursor = db.getInfo()
            if (cursor != null && cursor.moveToFirst()) {
                cursor.moveToFirst()
                nameTV.append(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_NAME)) + "\n")
                ageTV.append(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_AGE)) + "\n")
                roleTV.append(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_ROLE)) + "\n")
            }
            while (cursor!!.moveToNext()) {
                nameTV.append(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_NAME)) + "\n")
                ageTV.append(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_AGE)) + "\n")
                roleTV.append(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_ROLE)) + "\n")
            }
            cursor.close()
        }

        clearBTN.setOnClickListener {
            db.removeAll()
            nameTV.text = ""
            ageTV.text = ""
            roleTV.text = ""
        }

        setSupportActionBar(toolbarMain)
        title = "База данных"
        toolbarMain.subtitle = "version 1.0"
        toolbarMain.setLogo(R.drawable.ic_base_data)

    }

    private fun init() {
        toolbarMain = findViewById(R.id.toolbarMain)
        nameET = findViewById(R.id.nameET)
        ageET = findViewById(R.id.ageET)
        roleSpinnerS = findViewById(R.id.roleSpinnerS)
        saveBTN = findViewById(R.id.saveBTN)
        loadBTN = findViewById(R.id.loadBTN)
        clearBTN = findViewById(R.id.clearBTN)
        nameTV = findViewById(R.id.nameTV)
        ageTV = findViewById(R.id.ageTV)
        roleTV = findViewById(R.id.roleTV)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.exitMenuMain -> finishAffinity()
        }
        return super.onOptionsItemSelected(item)
    }
}