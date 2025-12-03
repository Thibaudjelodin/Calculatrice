package fr.eseo.b3.tj.appcalculatrice.ui.converter

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.math.BigDecimal
import java.math.RoundingMode

class ConverterViewModel : ViewModel() {

    // Taux de change basés sur l'Euro
    private val rates = mapOf(
        "EUR" to BigDecimal("1.0"),
        "USD" to BigDecimal("1.17"),
        "JPY" to BigDecimal("181.15"),
        "GBP" to BigDecimal("0.88")
    )

    private val _result = MutableStateFlow("")
    val result: StateFlow<String> = _result.asStateFlow()

    fun convert(amount: String, fromCurrency: String, toCurrency: String) {
        // Remplacer la virgule par un point pour la conversion
        val cleanAmount = amount.replace(",", ".")
        val amountValue = cleanAmount.toBigDecimalOrNull()
        if (amountValue == null) {
            _result.value = "Montant invalide"
            return
        }

        val fromRate = rates[fromCurrency]
        val toRate = rates[toCurrency]

        if (fromRate == null || toRate == null) {
            _result.value = "Devise non supportée"
            return
        }

        // Convertir le montant en Euro, puis de l'Euro vers la devise cible
        val amountInEur = amountValue.divide(fromRate, 4, RoundingMode.HALF_UP)
        val convertedAmount = amountInEur.multiply(toRate).setScale(2, RoundingMode.HALF_UP)

        _result.value = "$convertedAmount $toCurrency"
    }
}