package dev.firecrown.scriptest.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import dev.firecrown.scriptest.core.Script
import dev.firecrown.scriptest.data.entities.Output
import dev.firecrown.scriptest.data.json.ScriptHelper
import dev.firecrown.scriptest.data.repositories.BlockRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class ScriptestService: Service() {

    private val channelId = "scriptest_channel"
    private val channelName = "Scriptest Service"

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val foregroundId = intent
            ?.getIntExtra("foregroundId", 0)

        if(foregroundId != null && foregroundId != 0)
            startForeground(foregroundId, createNotification())

        BlockRepository.isScriptRunning.value = true

        val script = ScriptHelper()
            .parseScript(intent?.getStringExtra("script")?:"")


        val output = Output.valueOf(
            intent
                ?.getStringExtra("output")?:"DEFAULT"
        )

        BlockRepository.scriptName.value = script.name

        Script(this).execute(
            scriptText = script.script,
            output = output
        )

        CoroutineScope(Dispatchers.Main).launch {
            BlockRepository.isScriptRunning.collect {
                if(it != null && !it){
                    if(foregroundId != null && foregroundId != 0)
                        stopForeground(true)
                    stopSelf()
                    cancel()
                }
            }
        }

        return START_STICKY
    }

    private fun createNotification(): Notification {
        getSystemService(NotificationManager::class.java).createNotificationChannel(
            NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_LOW
            )
        )

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Scriptest")
            .setContentText("Test script is running right now")
            .setOngoing(true)
            .build()

    }
}