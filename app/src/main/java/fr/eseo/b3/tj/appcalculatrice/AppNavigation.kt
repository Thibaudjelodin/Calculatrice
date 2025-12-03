package fr.eseo.b3.tj.appcalculatrice

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import fr.eseo.b3.tj.appcalculatrice.ui.theme.CalculatorScreen
import fr.eseo.b3.tj.appcalculatrice.ui.theme.ColorTopBar
import fr.eseo.b3.tj.appcalculatrice.ui.theme.WhiteText

// 1. Définir les routes de navigation
sealed class Screen(val route: String, val title: String) {
    object Calculator : Screen("calculator", "Calculatrice")
    object Settings : Screen("settings", "Paramètres")
    object Scientist : Screen("scientist", "Scientifique")
}

private val screens = listOf(
    Screen.Calculator,
    Screen.Settings,
    Screen.Scientist
)

// 2. Créer des écrans de remplacement


@Composable
fun SettingsScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Paramètres")
    }
}
@Composable
fun ScientistScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Calculatrice Scientifique")
    }
}

// 3. Créer l'écran principal avec TopAppBar et onglets
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenWithTopBar() {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("Calculatrice App") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = ColorTopBar,
                        titleContentColor = WhiteText
                    )
                )

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                val selectedTabIndex = screens.indexOfFirst { it.route == currentRoute }.coerceAtLeast(0)

                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = ColorTopBar,
                    contentColor = WhiteText
                ) {
                    screens.forEach { screen ->
                        Tab(
                            text = { Text(screen.title) },
                            selected = currentRoute == screen.route,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Calculator.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Calculator.route) { CalculatorScreen() }
            composable(Screen.Settings.route) { SettingsScreen() }
            composable(Screen.Scientist.route) { ScientistScreen() }
        }
    }
}