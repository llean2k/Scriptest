package com.firecrowm.uxdebugtool

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.firecrowm.uxdebugtool.ui.theme.UxDebugToolTheme
import dev.firecrown.scriptest.core.Scriptest
import dev.firecrown.scriptest.core.TestLog
import dev.firecrown.scriptest.data.entities.Output
import dev.firecrown.scriptest.ui.ScriptestOverlay
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        enableEdgeToEdge()
        setContent {
            ScriptestOverlay {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    Button({
                        lifecycleScope.apply {
                            val scriptest = async {
                                return@async Scriptest(this@MainActivity)(
                                    script = "{\n" +
                                            "  \"name\": \"test1\",\n" +
                                            "  \"script\": [\n" +
                                            "    {\n" +
                                            "      \"title\": \"Тест\",\n" +
                                            "      \"text\": \"Нормально?\",\n" +
                                            "      \"dialogOverlayPosition\": \"Bottom\",\n" +
                                            "      \"textField\": null,\n" +
                                            "      \"pointer\": [0.3, 0.2, 0.3, 0.5],\n" +
                                            "      \"options\": [\"Да\", \"Нет\"],\n" +
                                            "      \"snapshot\": false\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"title\": \"Тест2\",\n" +
                                            "      \"text\": \"Точно Нормально?\",\n" +
                                            "      \"dialogOverlayPosition\": \"Center\",\n" +
                                            "      \"textField\": \"Введите количество ваших хромосом\",\n" +
                                            "      \"pointer\": [0.5, 0.2, 0.8, 0.1],\n" +
                                            "      \"options\": [\"Ну Да\", \"Неееет\"],\n" +
                                            "      \"snapshot\": false\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"title\": \"Тест3\",\n" +
                                            "      \"text\": \"Не бро ты уровен что все реально нормально???\",\n" +
                                            "      \"dialogOverlayPosition\": \"Сenter\",\n" +
                                            "      \"textField\": \"Мнение на счет Джон Гарика\",\n" +
                                            "      \"pointer\": [0.3, 0.2, 0.3, 0.5],\n" +
                                            "      \"snapshot\": false\n" +
                                            "    }\n" +
                                            "  ]\n" +
                                            "}",
                                    output = Output.SAVE,
                                    foregroundId = 67
                                )
                            }
                            launch {
                                scriptest.await()
                                Log.d("Scriptest", scriptest.await())
                            }
                        }
                    }) {
                        Text("Test")
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun TestUI(){
    Column(){
        Greeting(
            name = "Android"
        )
        Spacer(Modifier.height(5.dp))

        Greeting(
            name = "Android"
        )

        Spacer(Modifier.height(5.dp))

        Greeting(
            name = "Android"
        )
    }
}


