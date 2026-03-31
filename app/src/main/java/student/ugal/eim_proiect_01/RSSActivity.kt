package student.ugal.eim_proiect_01

import android.content.Intent
import android.os.Bundle
import android.util.Xml
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.xmlpull.v1.XmlPullParser
import student.ugal.eim_proiect_01.ui.theme.EIM_Proiect_01Theme
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.StringReader
import java.net.HttpURLConnection
import java.net.URL
import androidx.core.net.toUri

class RSSActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EIM_Proiect_01Theme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        AppBottomNavigationBar(currentScreen = NavigationScreen.RSS)
                    }
                ) { innerPadding ->
                    RSSView(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun RSSView(modifier: Modifier = Modifier) {
    var items by remember { mutableStateOf(listOf<Pair<String, String>>()) }
    var message by remember { mutableStateOf("Loading...") }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            try {
                val url = URL("https://sports.yahoo.com/rss/")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connectTimeout = 10000
                connection.readTimeout = 10000

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val reader = BufferedReader(InputStreamReader(connection.inputStream))
                    val content = reader.use { it.readText() }
                    val parsedItems = parseItems(content)
                    withContext(Dispatchers.Main) {
                        items = parsedItems
                        message = if (parsedItems.isEmpty()) "no items found." else ""
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        message = "server returned $responseCode"
                    }
                }
                connection.disconnect()
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    message = "eroare fetch: ${e.localizedMessage}"
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(text = "Latest Sport News:", style = MaterialTheme.typography.headlineSmall)

        if (message.isNotEmpty()) {
            Text(text = message, modifier = Modifier.padding(top = 16.dp))
        }

        items.forEach { item ->
            Text(text = "Title: ${item.first}", modifier = Modifier.padding(top = 16.dp))
            if (item.second.isNotEmpty()) {
                Text(
                    text = "Link: ${item.second}",
                    color = Color.Blue,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .clickable {
                            try {
                                val intent = Intent(Intent.ACTION_VIEW, item.second.toUri())
                                context.startActivity(intent)
                            } catch (_: Exception) {
                            }
                        }
                )
            }
        }
    }
}

fun parseItems(xml: String): List<Pair<String, String>> {
    val items = mutableListOf<Pair<String, String>>()

    try {
        val parser = Xml.newPullParser()
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
        parser.setInput(StringReader(xml))
        var eventType = parser.eventType
        var inItem = false
        var currentTitle = ""
        var currentLink = ""

        while (eventType != XmlPullParser.END_DOCUMENT) {
            val name = parser.name
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    if (name == "item") {
                        inItem = true
                        currentTitle = ""
                        currentLink = ""
                    } else if (inItem) {
                        when (name) {
                            "title" -> currentTitle = parser.nextText()
                            "link" -> currentLink = parser.nextText()
                        }
                    }
                }
                XmlPullParser.END_TAG -> {
                    if (name == "item") {
                        items.add(Pair(currentTitle, currentLink))
                        inItem = false
                    }
                }
            }
            eventType = parser.next()
        }
    } catch (_: Exception) {
    }

    return items
}
