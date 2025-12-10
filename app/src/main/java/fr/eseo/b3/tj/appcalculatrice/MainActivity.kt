package fr.eseo.b3.tj.appcalculatrice

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.os.LocaleListCompat
import fr.eseo.b3.tj.appcalculatrice.ui.theme.AppCalculatriceTheme
import fr.eseo.b3.tj.appcalculatrice.ui.settings.SettingsViewModel

class MainActivity : AppCompatActivity() {

    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val isDarkTheme by settingsViewModel.isDarkTheme.collectAsState()

            AppCalculatriceTheme(darkTheme = isDarkTheme) {
                MainScreenWithTopBar(onLocaleChange = { languageCode ->
                    val appLocale = LocaleListCompat.forLanguageTags(languageCode)
                    AppCompatDelegate.setApplicationLocales(appLocale)
                })
            }
        }
    }
}