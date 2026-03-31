package student.ugal.eim_proiect_01

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import student.ugal.eim_proiect_01.ui.theme.EIM_Proiect_01Theme

class ThirdActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EIM_Proiect_01Theme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        AppBottomNavigationBar(currentScreen = NavigationScreen.THIRD)
                    }
                ) { innerPadding ->
                    InterestContent(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun InterestContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.intern),
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
                .padding(bottom = 16.dp)
        )
        Text(
            text = stringResource(id = R.string.int_name),
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = stringResource(id = R.string.int_desc),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
