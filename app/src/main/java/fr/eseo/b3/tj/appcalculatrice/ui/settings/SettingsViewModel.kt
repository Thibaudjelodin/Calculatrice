package fr.eseo.b3.tj.appcalculatrice.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import fr.eseo.b3.tj.appcalculatrice.data.SettingsDataStore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val settingsDataStore = SettingsDataStore(application)

    // Exposer l'état du thème comme un StateFlow
    val isDarkTheme: StateFlow<Boolean> = settingsDataStore.isDarkTheme
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    // Fonction pour changer le thème
    fun setDarkTheme(isDark: Boolean) {
        viewModelScope.launch {
            settingsDataStore.setDarkTheme(isDark)
        }
    }

    // Exposer la langue comme un StateFlow
    val language: StateFlow<String> = settingsDataStore.language
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "fr"
        )

    // Fonction pour changer la langue
    fun setLanguage(language: String) {
        viewModelScope.launch {
            settingsDataStore.setLanguage(language)
        }
    }
}