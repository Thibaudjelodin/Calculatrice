package fr.eseo.b3.tj.appcalculatrice.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.eseo.b3.tj.appcalculatrice.R

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel = viewModel(),
    onLocaleChange: (String) -> Unit
) {
    val isDarkTheme by settingsViewModel.isDarkTheme.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(stringResource(id = R.string.settings_dark_mode))
            Switch(
                checked = isDarkTheme,
                onCheckedChange = { settingsViewModel.setDarkTheme(it) }
            )
        }

        Divider()

        LanguageSelector(settingsViewModel = settingsViewModel, onLocaleChange = onLocaleChange)
    }
}

@Composable
private fun LanguageSelector(
    settingsViewModel: SettingsViewModel,
    onLocaleChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val languages = mapOf(
        "fr" to stringResource(id = R.string.language_french),
        "en" to stringResource(id = R.string.language_english),
        "es" to stringResource(id = R.string.language_spanish),
        "de" to stringResource(id = R.string.language_german)
    )
    val currentLanguageCode by settingsViewModel.language.collectAsState()
    val currentLanguageName = languages[currentLanguageCode] ?: ""

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(stringResource(id = R.string.settings_language))

        Box {
            Button(onClick = { expanded = true }) {
                Text(currentLanguageName)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                languages.forEach { (code, name) ->
                    DropdownMenuItem(
                        text = { Text(name) },
                        onClick = {
                            settingsViewModel.setLanguage(code)
                            onLocaleChange(code)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}