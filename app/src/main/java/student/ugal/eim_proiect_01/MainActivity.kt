package student.ugal.eim_proiect_01

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import student.ugal.eim_proiect_01.ui.theme.EIM_Proiect_01Theme
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EIM_Proiect_01Theme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        AppBottomNavigationBar(currentScreen = NavigationScreen.OBIECTE)
                    }
                ) { innerPadding ->
                    StorageScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun StorageScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var textInput by remember { mutableStateOf("") }
    var floatValue by remember { mutableFloatStateOf(0f) }
    var fileContent by remember { mutableStateOf("") }
    val internalFileName = "internal_data.txt"
    val externalFileName = "external_data.txt"

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = textInput,
            onValueChange = { textInput = it },
            label = { Text("Introduceți text") },
            modifier = Modifier.fillMaxWidth()
        )

        Column {
            Text(text = "Valoare Float: ${String.format(Locale.getDefault(), "%.2f", floatValue)}")
            Slider(
                value = floatValue,
                onValueChange = { floatValue = it },
                valueRange = 0f..100f
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    val content = "$textInput | $floatValue"
                    saveToInternal(context, internalFileName, content)
                    textInput = ""
                    floatValue = 0f
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Scrie Intern")
            }
            Button(
                onClick = {
                    fileContent = readFromInternal(context, internalFileName)
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Citeste Intern")
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    val content = "$textInput | $floatValue"
                    saveToExternal(context, externalFileName, content)
                    textInput = ""
                    floatValue = 0f
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Scrie Extern")
            }
            Button(
                onClick = {
                    fileContent = readFromExternal(context, externalFileName)
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Citeste Extern")
            }
        }

        if (fileContent.isNotEmpty()) {
            Text(text = "Conținut fișier: $fileContent", modifier = Modifier.padding(top = 16.dp))
        }
    }
}

fun saveToInternal(context: Context, fileName: String, content: String) {
    try {
        val fos: FileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
        fos.write(content.toByteArray())
        fos.close()
        Toast.makeText(context, "Salvat intern", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        Toast.makeText(context, "Eroare salvare internă", Toast.LENGTH_SHORT).show()
    }
}

fun readFromInternal(context: Context, fileName: String): String {
    return try {
        val fis: FileInputStream = context.openFileInput(fileName)
        val content = fis.bufferedReader().use { it.readText() }
        fis.close()
        content
    } catch (e: Exception) {
        "Eroare citire internă"
    }
}

fun saveToExternal(context: Context, fileName: String, content: String) {
    try {
        val file = File(context.getExternalFilesDir(null), fileName)
        val fos = FileOutputStream(file)
        fos.write(content.toByteArray())
        fos.close()
        Toast.makeText(context, "Salvat extern", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        Toast.makeText(context, "Eroare salvare externă", Toast.LENGTH_SHORT).show()
    }
}

fun readFromExternal(context: Context, fileName: String): String {
    return try {
        val file = File(context.getExternalFilesDir(null), fileName)
        if (file.exists()) {
            val fis = FileInputStream(file)
            val content = fis.bufferedReader().use { it.readText() }
            fis.close()
            content
        } else {
            "Fișierul nu există"
        }
    } catch (e: Exception) {
        "Eroare citire externă"
    }
}

@Preview(showBackground = true)
@Composable
fun StorageScreenPreview() {
    EIM_Proiect_01Theme {
        StorageScreen()
    }
}
