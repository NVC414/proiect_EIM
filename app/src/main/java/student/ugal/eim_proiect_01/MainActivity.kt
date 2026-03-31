package student.ugal.eim_proiect_01

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentContainerView
import student.ugal.eim_proiect_01.ui.theme.EIM_Proiect_01Theme
import java.io.File
import java.util.Locale

class MainActivity : AppCompatActivity() {
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
                    StorageScreen(
                        modifier = Modifier.padding(innerPadding),
                        onNavigateToSecond = { key, value ->
                            val intent = Intent(this, FragmentDataReceiveAcitviity::class.java).apply {
                                putExtra("EXTRA_KEY", key)
                                putExtra("EXTRA_VALUE", value)
                            }
                            startActivity(intent)
                        },
                        activity = this
                    )
                }
            }
        }
    }
}

@Composable
fun StorageScreen(
    modifier: Modifier = Modifier,
    onNavigateToSecond: (String, Float) -> Unit,
    activity: AppCompatActivity
) {
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
            label = { Text("Introduceti Cheia (Text)") },
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

        Button(
            onClick = {
                onNavigateToSecond(textInput.trim(), floatValue)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Trimite Date la Ecranul 2")
        }

        Text(text = "fragment reutil", modifier = Modifier.padding(top = 8.dp))
        AndroidView(
            factory = { ctx ->
                FragmentContainerView(ctx).apply {
                    id = View.generateViewId()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            update = { view ->
                val fragment = DataDisplayFragment.newInstance(textInput, floatValue)
                activity.supportFragmentManager.beginTransaction()
                    .replace(view.id, fragment)
                    .commit()
            }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    val trimmedKey = textInput.trim()
                    if (trimmedKey.isNotBlank()) {
                        addEntry(context, internalFileName, trimmedKey, floatValue, true)
                        fileContent = readAll(context, internalFileName, true)
                    } else {
                        Toast.makeText(context, "Introduceti o cheie valida", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Adauga Intern")
            }
            Button(
                onClick = {
                    val trimmedKey = textInput.trim()
                    if (trimmedKey.isNotBlank()) {
                        removeEntry(context, internalFileName, trimmedKey, true)
                        fileContent = readAll(context, internalFileName, true)
                    } else {
                        Toast.makeText(context, "Introduceti cheia de sters", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Sterge Intern")
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
                        addEntry(context, externalFileName, trimmedKey, floatValue, false)
                        fileContent = readAll(context, externalFileName, false)
                    } else {
                        Toast.makeText(context, "Introduceti o cheie valida", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Adauga Extern")
            }
            Button(
                onClick = {
                    val trimmedKey = textInput.trim()
                    if (trimmedKey.isNotBlank()) {
                        removeEntry(context, externalFileName, trimmedKey, false)
                        fileContent = readAll(context, externalFileName, false)
                    } else {
                        Toast.makeText(context, "Introduceti cheia de sters", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Sterge Extern")
            }
        }

        Button(
            onClick = {
                val intContent = readAll(context, internalFileName, true)
                val extContent = readAll(context, externalFileName, false)
                fileContent = "--- Intern ---\n$intContent\n\n--- Extern ---\n$extContent"
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Vezi Tot Continutul")
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
    lines.removeAll { it.substringBefore('|').trim() == trimmedKey }
    lines.add("$trimmedKey|$value")
    writeAll(context, fileName, lines.joinToString("\n"), isInternal)
    Toast.makeText(context, "Adaugat cu succes", Toast.LENGTH_SHORT).show()
}

fun removeEntry(context: Context, fileName: String, key: String, isInternal: Boolean) {
    val trimmedKey = key.trim()
    val currentContent = readAll(context, fileName, isInternal)
    val lines = currentContent.lines().filter { it.isNotBlank() }.toMutableList()
    val initialSize = lines.size
    lines.removeAll { it.substringBefore('|').trim() == trimmedKey }
    if (lines.size < initialSize) {
        writeAll(context, fileName, lines.joinToString("\n"), isInternal)
        Toast.makeText(context, "Sters cu succes", Toast.LENGTH_SHORT).show()
    } else {
        Toast.makeText(context, "Cheia nu a fost gasita", Toast.LENGTH_SHORT).show()
    }
}
