package student.ugal.eim_proiect_01

import android.os.Bundle
import android.view.View
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentContainerView
import student.ugal.eim_proiect_01.ui.theme.EIM_Proiect_01Theme

class FragmentDataReceiveAcitviity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val key = intent.getStringExtra("EXTRA_KEY") ?: ""
        val value = intent.getFloatExtra("EXTRA_VALUE", 0f)

        setContent {
            EIM_Proiect_01Theme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        AppBottomNavigationBar(currentScreen = NavigationScreen.RSS)
                    }
                ) { innerPadding ->
                    DataReceiveScreen(
                        modifier = Modifier.padding(innerPadding),
                        activity = this,
                        key = key,
                        value = value
                    )
                }
            }
        }
    }
}

@Composable
fun DataReceiveScreen(
    modifier: Modifier = Modifier,
    activity: AppCompatActivity,
    key: String,
    value: Float
) {
    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Date Primite prin Fragment:")
        AndroidView(
            factory = { ctx ->
                FragmentContainerView(ctx).apply {
                    id = View.generateViewId()
                    val fragment = DataDisplayFragment.newInstance(key, value)
                    activity.supportFragmentManager.beginTransaction()
                        .replace(id, fragment)
                        .commit()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        )
    }
}
