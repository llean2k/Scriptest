package dev.firecrown.scriptest.data.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import dev.firecrown.scriptest.core.TestLog
import dev.firecrown.scriptest.data.entities.LogEntity

import dev.firecrown.scriptest.data.entities.ResultEntity
import dev.firecrown.scriptest.data.repositories.BlockRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class Db(context: Context) : SQLiteOpenHelper(context, "scriptest.db", null, 1) {

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL("""
            CREATE TABLE Result(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                textField TEXT,
                snapshot TEXT,
                option TEXT,
                exceptionMessage TEXT,
                timestamp TEXT
            )
        """.trimIndent())
    }

    fun deleteTable(){
        writableDatabase.apply {
            execSQL("DELETE FROM Result")
            execSQL("DELETE FROM sqlite_sequence WHERE name = 'Result'")
        }
    }

    fun insertResultBlock(result: ResultEntity){
        val values = ContentValues().apply {
            put("textField", result.textField)
            put("snapshot", result.snapshot)
            put("option", result.option)
            put("exceptionMessage", result.exceptionMessage)
            put("timestamp", result.timestamp)
        }
        writableDatabase
            .insert("Result", null, values)
    }

    fun getResult(): String {
        val gson = Gson()
        val json = JsonParser
            .parseString("{\"name\":\"${BlockRepository.scriptName.value}\",\"result\":[]}").asJsonObject

        writableDatabase.rawQuery("SELECT * FROM Result", null).apply {
            while (moveToNext()) {
                json.getAsJsonArray("result").add(
                    gson.toJsonTree(
                        ResultEntity(
                            textField = getString(getColumnIndexOrThrow("textField")),
                            snapshot = getString(getColumnIndexOrThrow("snapshot")),
                            option = getString(getColumnIndexOrThrow("option")),
                            exceptionMessage = getString(getColumnIndexOrThrow("exceptionMessage")),
                            timestamp = getString(getColumnIndexOrThrow("timestamp"))
                        )
                    )
                )
            }
            close()
        }
        json.add("logs", gson.toJsonTree(TestLog.logArray))
        return json.toString()
    }

    override fun onUpgrade(
        p0: SQLiteDatabase?,
        p1: Int,
        p2: Int
    ) {
        p0?.execSQL("DROP TABLE IF EXISTS Result")
        onCreate(p0)
    }

}