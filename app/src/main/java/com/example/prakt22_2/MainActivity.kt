package com.example.prakt22_2

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prakt22_2.API.ApiManager
import com.example.prakt22_2.Data.AppDatabase
import com.example.prakt22_2.Data.DataAdapter
import com.example.prakt22_2.Data.DataEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import androidx.appcompat.app.AlertDialog
import androidx.room.Room
import com.google.android.material.snackbar.Snackbar
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnFetchFact: Button
    private lateinit var editTextInput: EditText
    private lateinit var userDatabase: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        btnFetchFact = findViewById(R.id.btnFetchFact)
        editTextInput = findViewById(R.id.editTextInput)

        recyclerView.layoutManager = LinearLayoutManager(this)

        // Получаем имя базы данных из Intent
        val dbName = intent.getStringExtra("DB_NAME") ?: "default_database"
        userDatabase = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            dbName
        ).build()

        btnFetchFact.setOnClickListener {
            val input = editTextInput.text.toString()
            fetchAndSaveData(this, input)
        }

        displaySavedData(this, recyclerView)
    }

    private fun fetchAndSaveData(context: Context, input: String) {
        val apiManager = ApiManager(context)

        apiManager.fetchDataFromApi(input,
            { data ->
                val jsonObject = JSONObject(data)

                val name = jsonObject.getString("name")

                val sprites = jsonObject.getJSONObject("sprites")
                val imageUrl = sprites.getString("front_default")

                val abilitiesArray = jsonObject.getJSONArray("abilities")
                val abilitiesList = (0 until abilitiesArray.length()).map { index ->
                    val abilityObject = abilitiesArray.getJSONObject(index).getJSONObject("ability")
                    abilityObject.getString("name")
                }.toTypedArray()

                CoroutineScope(Dispatchers.IO).launch {
                    val imageBytes = fetchImageBytes(imageUrl)
                    val dataEntity = DataEntity(
                        name = name,
                        abilities = abilitiesList,
                        imageBytes = imageBytes
                    )
                    userDatabase.dataDao().insertData(dataEntity)
                    withContext(Dispatchers.Main) {
                        displaySavedData(context, recyclerView)
                    }
                }
            },
            { error ->
                showSnackbar("Не удалось найти запрос. Попробуйте снова.")
            }
        )
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show()
    }

    private fun fetchImageBytes(imageUrl: String): ByteArray? {
        return try {
            val url = URL(imageUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.inputStream.use { inputStream ->
                return inputStream.readBytes()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun displaySavedData(context: Context, recyclerView: RecyclerView) {
        CoroutineScope(Dispatchers.IO).launch {
            val dataList = userDatabase.dataDao().getAllData()

            withContext(Dispatchers.Main) {
                val adapter = DataAdapter(dataList) { selectedData ->
                    showOptionsDialog(selectedData)
                }
                recyclerView.adapter = adapter
            }
        }
    }

    private fun showOptionsDialog(data: DataEntity) {
        val options = arrayOf("Редактировать", "Удалить")

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Выберите действие")
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> editData(data)
                1 -> deleteData(data)
            }
        }
        builder.show()
    }

    private fun deleteData(data: DataEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            userDatabase.dataDao().deleteData(data)
            withContext(Dispatchers.Main) {
                displaySavedData(this@MainActivity, recyclerView)
            }
        }
    }

    private fun editData(data: DataEntity) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Редактировать имя")
        
        val input = EditText(this)
        input.setText(data.name)
        builder.setView(input)

        builder.setPositiveButton("Сохранить") { dialog, _ ->
            val newName = input.text.toString()
            if (newName.isNotEmpty()) {
                CoroutineScope(Dispatchers.IO).launch {
                    val updatedData = data.copy(name = newName)
                    userDatabase.dataDao().updateData(updatedData)

                    withContext(Dispatchers.Main) {
                        displaySavedData(this@MainActivity, recyclerView)
                        showSnackbar("Имя обновлено!")
                    }
                }
            } else {
                showSnackbar("Имя не может быть пустым!")
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("Отмена") { dialog, _ ->
            dialog.dismiss()
        }

        builder.show()
    }

}
