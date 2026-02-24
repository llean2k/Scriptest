package dev.firecrown.scriptest.core

import android.content.Context
import android.content.Intent
import android.util.Log
import dev.firecrown.scriptest.data.entities.Output
import dev.firecrown.scriptest.data.entities.ResultEntity
import dev.firecrown.scriptest.data.json.ScriptHelper
import dev.firecrown.scriptest.data.media.OutputScript
import dev.firecrown.scriptest.data.repositories.BlockRepository
import dev.firecrown.scriptest.services.ScriptestService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import java.text.SimpleDateFormat
import java.util.Date

class Scriptest(
    val context: Context
) {
    suspend operator fun invoke(
        script: String,
        output: Output,
        foregroundId: Int? = null

    ): String {
        context
            .startService(Intent(context, ScriptestService::class.java).apply {
                putExtra("script", script)
                putExtra("output", output.name)

                if(foregroundId != null)
                    putExtra("foregroundId", foregroundId)
            })

        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            BlockRepository.apply {
                resultList.value.add(
                    ResultEntity(
                        textField = textField.value,
                        snapshot = snapshot.value,
                        option = option.value,
                        exceptionMessage = throwable.stackTraceToString(),
                        timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
                    )
                )
                OutputScript(context).file()
                defaultHandler?.uncaughtException(thread, throwable)
            }
        }

        BlockRepository.isScriptRunning.first{it != null && !it}
        BlockRepository.isScriptRunning.value = null

        Log.d("ScriptTest", TestLog.logArray.toString())

        return ScriptHelper().createResult(
            BlockRepository.resultList.value,
            TestLog.logArray,
            BlockRepository.scriptName.value
        )
    }
}