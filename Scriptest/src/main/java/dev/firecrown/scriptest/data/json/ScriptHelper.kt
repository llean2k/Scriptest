package dev.firecrown.scriptest.data.json

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dev.firecrown.scriptest.core.Script
import dev.firecrown.scriptest.data.entities.LogEntity
import dev.firecrown.scriptest.data.entities.ResultEntity
import dev.firecrown.scriptest.data.entities.BlockEntity
import dev.firecrown.scriptest.data.entities.ScriptEntity
import dev.firecrown.scriptest.data.repositories.BlockRepository.scriptName
import java.io.File

internal class ScriptHelper(val context: Context) {

    private val gson = Gson()

    fun parseScript(script: String): ScriptEntity {
        return gson.fromJson(script, ScriptEntity::class.java)
    }

    fun createResult(
        resultList: List<ResultEntity>,
        logsList: List<LogEntity>,
        name: String
    ): String {
        val result = mapOf(
            "name" to name,
            "result" to resultList,
            "logs" to logsList
        )
        return gson.toJson(result)
    }

}