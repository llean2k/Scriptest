package dev.firecrown.scriptest.ui

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.firecrown.scriptest.data.repositories.BlockRepository

@Composable
fun ScriptestOverlay(content: @Composable () -> Unit){
    val pointer = BlockRepository.pointer.collectAsState().value
    val isScriptRunning = BlockRepository.isScriptRunning.collectAsState().value
    val dialogOverlayPosition = BlockRepository.dialogOverlayPosition.collectAsState().value
    val snapshot = BlockRepository.snapshot.collectAsState().value
    val isOverlayVisible = BlockRepository.isOverlayVisible.collectAsState().value
    val title = BlockRepository.title.collectAsState().value

    Log.d("Scriptest", "isOverlayVisible: $isOverlayVisible")
    content()
    if(isScriptRunning != null && title != "Title"){

        if(snapshot == "snapshot"){
            TakeSnapshot {
                BlockRepository.snapshot.value = it
            }
        }

        if(pointer != null && pointer.size == 4){
            PointerOverlay(
                start = pointer[0],
                end = pointer[1],
                top = pointer[2],
                bottom = pointer[3]
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = when(dialogOverlayPosition){
                "Center" -> Alignment.Center
                "Top" -> Alignment.TopCenter
                else -> Alignment.BottomCenter
            }
        ){
            Column {
                if(dialogOverlayPosition == "Top"){
                    Spacer(modifier = Modifier.padding(top = 70.dp))
                }

                DialogOverlay()

                if(dialogOverlayPosition == "Bottom"){
                    Spacer(modifier = Modifier.padding(bottom = 70.dp))
                }

            }
        }
    }else{
        Box(modifier = Modifier)
    }

}