package student.ugal.eim_proiect_01

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
                    ObiecteScreen(modifier = Modifier.padding(innerPadding))
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
fun ObiecteItem(o: Obiecte, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(8.dp)) {
       if(!o.esteKotlin)
       {
           Text(text = "Numar Long: ${o.numar}")
           Text(text = "Numar Double: ${o.numarFloat}")
           Text(text = "Caracter: ${o.caracter}")
           Text(text = "Este Kotlin: ${o.esteKotlin}")
           Text(text = "Brand: ${o.brand}")
       }
//           Text(text = "Numar Long: ${o.numar}")
//        Text(text = "Numar Double: ${o.numarFloat}")
//        Text(text = "Caracter: ${o.caracter}")
//        Text(text = "Este Kotlin: ${o.esteKotlin}")
//        Text(text = "Brand: ${o.brand}")
    }
}

@Composable
fun ObiecteScreen(modifier: Modifier = Modifier) {
    val lista = listOf(
        Obiecte(2147483648L, 14E2, 'A', true, "Samsung"),
        Obiecte(12L, 3.14, 'B', false, "Apple"),
        Obiecte(999L, 42.0, 'C', true, "Xiaomi"),
        Obiecte(0L, 0.0, 'D', false, "Nothing"),
        Obiecte(7L, 2.718, 'E', true, "Google"),
        Obiecte(1L, 1.618, 'F', false, "OnePlus"),
    )
    
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        lista.forEach { o ->
            ObiecteItem(o)
        }
    }
}

/*
@Composable
fun Point1(Numar: Long,NumarFloat:Float,Character:Char,isThisKotlin:Boolean,phoneBrands: Array<String>,modifier:Modifier=Modifier)
{
Text(
text="Numar Long : $Numar",
modifier = modifier
)
Text(
text="Numar Float : $NumarFloat",
modifier = modifier
)
Text(
text="Character : $Character",
modifier = modifier
)
Text(
text="isThisKotlin : $isThisKotlin",
modifier = modifier
)
Text(
text="phoneBrands : $phoneBrands",
modifier = modifier
)
}
*/

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EIM_Proiect_01Theme {
        Greeting("Android")
    }
}

@Preview(showBackground = true)
@Composable
fun ObiecteScreenPreview() {
    EIM_Proiect_01Theme {
        ObiecteScreen()
    }
}