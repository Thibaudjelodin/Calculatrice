package fr.eseo.b3.tj.appcalculatrice.ui.calculator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.eseo.b3.tj.appcalculatrice.ui.theme.ColorButton
import fr.eseo.b3.tj.appcalculatrice.ui.theme.ColorText

@Composable
fun CalculatorScreen(modifier: Modifier = Modifier) {
    var expression by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Affichage du résultat / expression
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = result.ifEmpty { expression.ifEmpty { "0" } },
                color = MaterialTheme.colorScheme.onSurface, // Correction : utiliser la couleur du thème
                fontSize = 40.sp,
                textAlign = TextAlign.Right,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Boutons en bas
        val buttons = listOf(
            listOf("7", "8", "9", "÷"),
            listOf("4", "5", "6", "×"),
            listOf("1", "2", "3", "-"),
            listOf("0", ".", "+", "⌫"),
            listOf("C", "=")
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            buttons.forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    row.forEach { label ->
                        val buttonModifier = if (label == "=") {
                            Modifier
                                .weight(2f)
                                .height(56.dp)
                        } else {
                            Modifier
                                .weight(1f)
                                .height(56.dp)
                        }
                        CalculatorButton(
                            label = label,
                            onClick = {
                                when (label) {
                                    "C" -> {
                                        expression = ""
                                        result = ""
                                    }
                                    "⌫" -> {
                                        if (expression.isNotEmpty()) {
                                            expression = expression.dropLast(1)
                                        }
                                        result = ""
                                    }
                                    "=" -> {
                                        result = evaluateExpression(expression)
                                    }
                                    else -> {
                                        if (isOperator(label)) {
                                            if (expression.isNotEmpty() && !isOperator(expression.last().toString())) {
                                                expression += label
                                            }
                                        } else {
                                            expression += label
                                        }
                                        result = ""
                                    }
                                }
                            },
                            modifier = buttonModifier
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CalculatorButton(label: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(containerColor = ColorButton, contentColor = ColorText)
    ) {
        Text(text = label, fontSize = 20.sp)
    }
}

private fun isOperator(s: String): Boolean = s in listOf("+", "-", "×", "÷", "*", "/",".")

private fun evaluateExpression(expr: String): String {
    if (expr.isBlank()) return ""
    try {
        val normalized = expr.replace('×', '*').replace('÷', '/')

        val tokens = mutableListOf<String>()
        var i = 0
        while (i < normalized.length) {
            val c = normalized[i]
            if (c.isDigit() || c == '.') {
                val sb = StringBuilder()
                while (i < normalized.length && (normalized[i].isDigit() || normalized[i] == '.')) {
                    sb.append(normalized[i])
                    i++
                }
                tokens.add(sb.toString())
            } else if (c == '+' || c == '-' || c == '*' || c == '/') {
                tokens.add(c.toString())
                i++
            } else {
                i++
            }
        }

        if (tokens.isEmpty()) return ""

        val mdList = mutableListOf<String>()
        var idx = 0
        while (idx < tokens.size) {
            val t = tokens[idx]
            if (t == "*" || t == "/") {
                val prev = mdList.removeAt(mdList.lastIndex).toDouble()
                val next = tokens[idx + 1].toDouble()
                val res = if (t == "*") prev * next else prev / next
                mdList.add(res.toString())
                idx += 2
            } else {
                mdList.add(t)
                idx++
            }
        }

        var result = mdList[0].toDouble()
        idx = 1
        while (idx < mdList.size) {
            val op = mdList[idx]
            val num = mdList[idx + 1].toDouble()
            result = if (op == "+") result + num else result - num
            idx += 2
        }

        return if (result % 1.0 == 0.0) result.toLong().toString() else result.toString()
    } catch (e: Exception) {
        return "Error"
    }
}

@Preview(showBackground = true)
@Composable
private fun CalculatorPreview() {
    CalculatorScreen()
}
