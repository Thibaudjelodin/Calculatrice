package fr.eseo.b3.tj.appcalculatrice.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Spacer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable

@Composable
fun CalculatorScreen(modifier: Modifier = Modifier) {
    var expression by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BlackBackground)
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
                text = if (result.isNotEmpty()) result else expression.ifEmpty { "0" },
                color = WhiteText,
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
            listOf("C", "0", ".", "+"),
            listOf("=")
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
                        CalculatorButton(
                            label = label,
                            onClick = {
                                when (label) {
                                    "C" -> {
                                        expression = ""
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
                            modifier = if (label == "=") Modifier
                                .weight(1f)
                                .height(56.dp)
                            else Modifier.weight(1f).height(56.dp)
                        )
                    }

                    // Si la ligne ne remplit pas l'espace (cas "=" seul), ajouter un spacer
                    if (row.size == 1) {
                        Spacer(modifier = Modifier.weight(3f))
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
        colors = ButtonDefaults.buttonColors(containerColor = GreenButton, contentColor = WhiteText)
    ) {
        Text(text = label, fontSize = 20.sp)
    }
}

private fun isOperator(s: String): Boolean = s in listOf("+", "-", "×", "÷", "*", "/")

private fun evaluateExpression(expr: String): String {
    if (expr.isBlank()) return ""
    try {
        // Normaliser les opérateurs
        val normalized = expr.replace('×', '*').replace('÷', '/')

        // Tokenize
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
                // ignorer autres caractères
                i++
            }
        }

        if (tokens.isEmpty()) return ""

        // Traitement * et /
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

        // Traitement + et -
        var result = mdList[0].toDouble()
        idx = 1
        while (idx < mdList.size) {
            val op = mdList[idx]
            val num = mdList[idx + 1].toDouble()
            result = if (op == "+") result + num else result - num
            idx += 2
        }

        // Formatage: enlever .0 si entier
        return if (result % 1.0 == 0.0) result.toLong().toString() else result.toString()
    } catch (e: Exception) {
        return "Erreur"
    }
}

@Preview(showBackground = true)
@Composable
private fun CalculatorPreview() {
    CalculatorScreen()
}