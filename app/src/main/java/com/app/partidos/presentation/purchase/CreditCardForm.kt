package com.app.partidos.presentation.purchase

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.app.partidos.R

class CardNumberVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= 16) text.text.substring(0..15) else text.text
        var out = ""
        for (i in trimmed.indices) {
            out += trimmed[i]
            if (i % 4 == 3 && i != 15) out += " "
        }
        val offsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 3) return offset
                if (offset <= 7) return offset + 1
                if (offset <= 11) return offset + 2
                if (offset <= 16) return offset + 3
                return 19
            }
            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 4) return offset
                if (offset <= 9) return offset - 1
                if (offset <= 14) return offset - 2
                if (offset <= 19) return offset - 3
                return 16
            }
        }
        return TransformedText(AnnotatedString(out), offsetTranslator)
    }
}

class ExpirationDateVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= 4) text.text.substring(0..3) else text.text
        var out = ""
        for (i in trimmed.indices) {
            out += trimmed[i]
            if (i == 1) out += "/"
        }
        val offsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 1) return offset
                if (offset <= 4) return offset + 1
                return 5
            }
            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 2) return offset
                if (offset <= 5) return offset - 1
                return 4
            }
        }
        return TransformedText(AnnotatedString(out), offsetTranslator)
    }
}

@Composable
fun CreditCardForm(
    numeroTarjeta: String,
    onTarjetaChanged: (String) -> Unit,
    nombreTitular: String,
    onTitularChanged: (String) -> Unit,
    vencimiento: String,
    onVencimientoChanged: (String) -> Unit,
    cvv: String,
    onCvvChanged: (String) -> Unit,
    textFieldColors: TextFieldColors
) {
    OutlinedTextField(
        value = numeroTarjeta,
        onValueChange = onTarjetaChanged,
        label = { Text(stringResource(R.string.purchase_card_number)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        visualTransformation = CardNumberVisualTransformation(),
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        colors = textFieldColors
    )

    OutlinedTextField(
        value = nombreTitular,
        onValueChange = onTitularChanged,
        label = { Text(stringResource(R.string.purchase_card_name)) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        colors = textFieldColors
    )

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
            value = vencimiento,
            onValueChange = onVencimientoChanged,
            label = { Text(stringResource(R.string.purchase_card_expiration)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            visualTransformation = ExpirationDateVisualTransformation(),
            singleLine = true,
            modifier = Modifier.weight(1f),
            colors = textFieldColors
        )
        OutlinedTextField(
            value = cvv,
            onValueChange = onCvvChanged,
            label = { Text(stringResource(R.string.purchase_card_cvv)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.weight(1f),
            colors = textFieldColors
        )
    }
}
