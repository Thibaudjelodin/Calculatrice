package fr.eseo.b3.tj.appcalculatrice.ui.converter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.eseo.b3.tj.appcalculatrice.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConverterScreen(converterViewModel: ConverterViewModel = viewModel()) {
    var amount by remember { mutableStateOf("") }
    var fromCurrency by remember { mutableStateOf("EUR") }
    var toCurrency by remember { mutableStateOf("USD") }
    val result by converterViewModel.result.collectAsState()

    val currencies = mapOf(
        "EUR" to stringResource(id = R.string.currency_eur),
        "USD" to stringResource(id = R.string.currency_usd),
        "JPY" to stringResource(id = R.string.currency_jpy),
        "GBP" to stringResource(id = R.string.currency_gbp)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(stringResource(id = R.string.screen_title_converter), fontSize = 24.sp)

        TextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text(stringResource(id = R.string.converter_amount)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            CurrencySelector(label = stringResource(id = R.string.converter_from), selectedCurrency = fromCurrency, onCurrencyChange = { fromCurrency = it }, currencies = currencies, modifier = Modifier.weight(1f))
            CurrencySelector(label = stringResource(id = R.string.converter_to), selectedCurrency = toCurrency, onCurrencyChange = { toCurrency = it }, currencies = currencies, modifier = Modifier.weight(1f))
        }

        Button(onClick = { converterViewModel.convert(amount, fromCurrency, toCurrency) }) {
            Text(stringResource(id = R.string.converter_button))
        }

        if (result.isNotEmpty()) {
            Text(
                text = result, 
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CurrencySelector(
    label: String,
    selectedCurrency: String,
    onCurrencyChange: (String) -> Unit,
    currencies: Map<String, String>,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }, modifier = modifier) {
        TextField(
            value = currencies[selectedCurrency] ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            currencies.forEach { (code, name) ->
                DropdownMenuItem(
                    text = { Text(name) },
                    onClick = {
                        onCurrencyChange(code)
                        expanded = false
                    }
                )
            }
        }
    }
}
