package fr.eseo.b3.tj.appcalculatrice

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import fr.eseo.b3.tj.appcalculatrice.ui.calculator.CalculatorScreen
import fr.eseo.b3.tj.appcalculatrice.ui.converter.ConverterScreen 
import fr.eseo.b3.tj.appcalculatrice.ui.settings.SettingsScreen
import fr.eseo.b3.tj.appcalculatrice.ui.theme.ColorButton
import fr.eseo.b3.tj.appcalculatrice.ui.theme.ColorText

sealed class Screen(val route: String, @StringRes val titleResId: Int) {
    object Calculator : Screen("calculator", R.string.tab_calculator)
    object Settings : Screen("settings", R.string.tab_settings)
    object Converter : Screen("converter", R.string.tab_converter)
}

private val screens = listOf(Screen.Calculator, Screen.Settings, Screen.Converter)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenWithTopBar(onLocaleChange: (String) -> Unit) {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text(stringResource(id = R.string.app_name)) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = ColorButton,
                        titleContentColor = ColorText
                    )
                )

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                val selectedTabIndex = screens.indexOfFirst { it.route == currentRoute }.coerceAtLeast(0)

                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = ColorButton,
                    contentColor = ColorText
                ) {
                    screens.forEach { screen ->
                        Tab(
                            text = { Text(stringResource(id = screen.titleResId)) },
                            selected = currentRoute == screen.route,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
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
            composable(Screen.Settings.route) { SettingsScreen(onLocaleChange = onLocaleChange) }
            composable(Screen.Converter.route) { ConverterScreen() }
        }
    }
}