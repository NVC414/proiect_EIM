package student.ugal.eim_proiect_01

import android.app.Activity
import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

enum class NavigationScreen {
    OBIECTE, RSS, THIRD
}

@Composable
fun AppBottomNavigationBar(currentScreen: NavigationScreen) {
    val context = LocalContext.current
    NavigationBar {
        NavigationBarItem(
            selected = currentScreen == NavigationScreen.OBIECTE,
            onClick = {
                if (currentScreen != NavigationScreen.OBIECTE) {
                    context.startActivity(Intent(context, MainActivity::class.java))
                }
            },
            icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = null) },
            label = { Text("Obiecte") }
        )
        NavigationBarItem(
            selected = currentScreen == NavigationScreen.RSS,
            onClick = {
                if (currentScreen != NavigationScreen.RSS) {
                    context.startActivity(Intent(context, RSSActivity::class.java))
                }
            },
            icon = { Icon(Icons.Default.Refresh, contentDescription = null) },
            label = { Text("RSS") }
        )
        NavigationBarItem(
            selected = currentScreen == NavigationScreen.THIRD,
            onClick = {
                if (currentScreen != NavigationScreen.THIRD) {
                    context.startActivity(Intent(context, ThirdActivity::class.java))
                }
            },
            icon = { Icon(Icons.Default.Info, contentDescription = null) },
            label = { Text("Internationalizare") }
        )
    }
}
