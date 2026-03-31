package student.ugal.eim_proiect_01

import android.os.Bundle
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
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import student.ugal.eim_proiect_01.ui.theme.EIM_Proiect_01Theme

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
                    ObiecteScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun ObiecteItem(o: Obiecte, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(8.dp)) {
        Text(text = "Numar Long: ${o.numar}")
        Text(text = "Numar Double: ${o.numarFloat}")
        Text(text = "Caracter: ${o.caracter}")
        Text(text = "Este Kotlin: ${o.esteKotlin}")
        Text(text = "Brand: ${o.brand} (Length: ${o.brand.length})")
    }
}

@Composable
fun ObiecteScreen(modifier: Modifier = Modifier) {
    var selectedIsKotlin by remember { mutableStateOf(true) }
    var sliderValue by remember { mutableFloatStateOf(0f) }


    val lista = listOf(
        Obiecte(2147483648L, 14E2, 'A', true, "Samsung"),
        Obiecte(12L, 3.14, 'B', false, "Apple"),
        Obiecte(999L, 42.0, 'C', true, "Xiaomi"),
        Obiecte(0L, 0.0, 'D', false, "Nothing"),
        Obiecte(7L, 2.718, 'E', true, "Google"),
        Obiecte(1L, 1.618, 'F', false, "OnePlus"),
        Obiecte(100L, 2.0, 'G', true, "Some"),
        Obiecte(200L, 3.0, 'H', false, "Ting"),
    )

    val filteredLista = lista.filter { o ->
       // val lengthMatch = maxLengthInput.toIntOrNull()?.let { o.brand.length <= it } ?: true
        val sliderValueInt = sliderValue.toInt()
        val lengthMatch = o.brand.length <= sliderValueInt
        val kotlinMatch = o.esteKotlin == selectedIsKotlin
        lengthMatch && kotlinMatch
    }
    
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text="Slider value max lenght:${sliderValue.toInt()}")

        Slider(
            value = sliderValue,
            onValueChange = { sliderValue = it },
            valueRange = 0f..10f
        )


        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Is Kotlin:")
            RadioButton(
                selected = selectedIsKotlin,
                onClick = { selectedIsKotlin = true }
            )
            Text("True")
            RadioButton(
                selected = !selectedIsKotlin,
                onClick = { selectedIsKotlin = false }
            )
            Text("False")
        }

        filteredLista.forEach { o ->
            ObiecteItem(o)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ObiecteScreenPreview() {
    EIM_Proiect_01Theme {
        ObiecteScreen()
    }
}
