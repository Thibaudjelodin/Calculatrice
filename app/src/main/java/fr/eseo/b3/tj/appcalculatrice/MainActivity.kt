package fr.eseo.b3.tj.appcalculatrice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import fr.eseo.b3.tj.appcalculatrice.ui.theme.AppCalculatriceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppCalculatriceTheme {
                MainScreenWithTopBar()
            }
        }
    }
}