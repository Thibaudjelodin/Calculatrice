package fr.eseo.b3.tj.appcalculatrice.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extension pour créer une instance de DataStore
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsDataStore(context: Context) {

    private val dataStore = context.dataStore

    // Clé pour le mode sombre
    private val isDarkThemeKey = booleanPreferencesKey("is_dark_theme")

    // Flow pour observer le changement de thème
    val isDarkTheme: Flow<Boolean> = dataStore.data.map {
        it[isDarkThemeKey] ?: false // Par défaut, le thème clair est utilisé
    }

    // Fonction pour mettre à jour le thème
    suspend fun setDarkTheme(isDark: Boolean) {
        dataStore.edit {
            it[isDarkThemeKey] = isDark
        }
    }

    // Clé pour la langue
    private val languageKey = stringPreferencesKey("language")

    // Flow pour observer la langue
    val language: Flow<String> = dataStore.data.map {
        it[languageKey] ?: "fr" // Français par défaut
    }

    // Fonction pour mettre à jour la langue
    suspend fun setLanguage(language: String) {
        dataStore.edit {
            it[languageKey] = language
        }
    }
}