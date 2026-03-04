package dev.firecrown.scriptest.data.media

import android.content.Context
import dev.firecrown.scriptest.core.TestLog
import dev.firecrown.scriptest.data.db.Db

import dev.firecrown.scriptest.data.repositories.BlockRepository.scriptName
import java.io.File

internal class OutputScript(val context: Context) {

    fun file() {
        val file = File(
            context.getExternalFilesDir("scripts"),
            "scriptest-${scriptName.value}.json"
        )
        file.writeText(
            Db(context).getResult()
        )
    }
}