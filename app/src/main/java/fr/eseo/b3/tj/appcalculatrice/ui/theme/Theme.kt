package fr.eseo.b3.tj.appcalculatrice.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = ColorButton,
    background = ColorBackground,
    surface = ColorBackground,
    onPrimary = ColorText,
    onBackground = ColorText,
    onSurface = ColorText // Le texte sur une surface sombre est blanc
)

private val LightColorScheme = lightColorScheme(
    primary = ColorButton,
    background = Color.White,
    surface = Color.White,
    onPrimary = ColorText,
    onBackground = Color.Black,
    onSurface = Color.Black // Le texte sur une surface claire est noir
)

@Composable
fun AppCalculatriceTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}