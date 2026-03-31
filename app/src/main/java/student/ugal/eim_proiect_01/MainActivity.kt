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
            label = { Text("Introduceți Cheia (Text)") },
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
                    val trimmedKey = textInput.trim()
                    if (trimmedKey.isNotBlank()) {
                        addEntry(context, internalFileName, trimmedKey, floatValue, isInternal = true)
                        textInput = ""
                        floatValue = 0f
                        fileContent = readAll(context, internalFileName, isInternal = true)
                    } else {
                        Toast.makeText(context, "Introduceți o cheie validă", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Adaugă Intern")
            }
            Button(
                onClick = {
                    val trimmedKey = textInput.trim()
                    if (trimmedKey.isNotBlank()) {
                        removeEntry(context, internalFileName, trimmedKey, isInternal = true)
                        textInput = ""
                        fileContent = readAll(context, internalFileName, isInternal = true)
                    } else {
                        Toast.makeText(context, "Introduceți cheia de șters", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Șterge Intern")
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    val trimmedKey = textInput.trim()
                    if (trimmedKey.isNotBlank()) {
                        addEntry(context, externalFileName, trimmedKey, floatValue, isInternal = false)
                        textInput = ""
                        floatValue = 0f
                        fileContent = readAll(context, externalFileName, isInternal = false)
                    } else {
                        Toast.makeText(context, "Introduceți o cheie validă", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Adaugă Extern")
            }
            Button(
                onClick = {
                    val trimmedKey = textInput.trim()
                    if (trimmedKey.isNotBlank()) {
                        removeEntry(context, externalFileName, trimmedKey, isInternal = false)
                        textInput = ""
                        fileContent = readAll(context, externalFileName, isInternal = false)
                    } else {
                        Toast.makeText(context, "Introduceți cheia de șters", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Șterge Extern")
            }
        }

        Button(
            onClick = {
                val intContent = readAll(context, internalFileName, isInternal = true)
                val extContent = readAll(context, externalFileName, isInternal = false)
                fileContent = "--- Intern ---\n$intContent\n\n--- Extern ---\n$extContent"
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Vezi Tot Conținutul")
        }

        if (fileContent.isNotEmpty()) {
            Text(text = fileContent, modifier = Modifier.padding(top = 16.dp))
        }
    }
}

fun getFile(context: Context, fileName: String, isInternal: Boolean): File {
    return if (isInternal) {
        File(context.filesDir, fileName)
    } else {
        File(context.getExternalFilesDir(null), fileName)
    }
}

fun readAll(context: Context, fileName: String, isInternal: Boolean): String {
    val file = getFile(context, fileName, isInternal)
    return if (file.exists()) {
        try {
            file.readText()
        } catch (e: Exception) {
            "Eroare citire"
        }
    } else {
        ""
    }
}

fun writeAll(context: Context, fileName: String, content: String, isInternal: Boolean) {
    val file = getFile(context, fileName, isInternal)
    try {
        file.writeText(content)
    } catch (e: Exception) {
        Toast.makeText(context, "Eroare scriere", Toast.LENGTH_SHORT).show()
    }
}

fun addEntry(context: Context, fileName: String, key: String, value: Float, isInternal: Boolean) {
    val trimmedKey = key.trim()
    val currentContent = readAll(context, fileName, isInternal)
    val lines = currentContent.lines().filter { it.isNotBlank() }.toMutableList()
    
    // Robust check: split by '|' and trim the key part to handle legacy data with spaces
    lines.removeAll { it.substringBefore('|').trim() == trimmedKey }
    
    // Add new entry in standardized format
    lines.add("$trimmedKey|$value")
    
    writeAll(context, fileName, lines.joinToString("\n"), isInternal)
    Toast.makeText(context, "Adăugat cu succes", Toast.LENGTH_SHORT).show()
}

fun removeEntry(context: Context, fileName: String, key: String, isInternal: Boolean) {
    val trimmedKey = key.trim()
    val currentContent = readAll(context, fileName, isInternal)
    val lines = currentContent.lines().filter { it.isNotBlank() }.toMutableList()
    
    val initialSize = lines.size
    // Robust check: split by '|' and trim the key part
    lines.removeAll { it.substringBefore('|').trim() == trimmedKey }
    
    if (lines.size < initialSize) {
        writeAll(context, fileName, lines.joinToString("\n"), isInternal)
        Toast.makeText(context, "Șters cu succes", Toast.LENGTH_SHORT).show()
    } else {
        Toast.makeText(context, "Cheia nu a fost găsită", Toast.LENGTH_SHORT).show()
    }
}

@Preview(showBackground = true)
@Composable
fun StorageScreenPreview() {
    EIM_Proiect_01Theme {
        StorageScreen()
    }
}
