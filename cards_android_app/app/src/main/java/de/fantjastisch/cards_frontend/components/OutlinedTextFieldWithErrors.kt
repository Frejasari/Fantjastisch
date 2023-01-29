package de.fantjastisch.cards_frontend.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import de.fantjastisch.cards_frontend.card.update.mapError
import org.openapitools.client.models.ErrorEntryEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedTextFieldWithErrors(
    maxLines: Int = 1,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    errors: List<ErrorEntryEntity>,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    field: String
) {
    val error = errors.find { it.field == field }
    OutlinedTextField(
        singleLine = maxLines == 1,
        maxLines = maxLines,
        modifier = Modifier.fillMaxWidth(),
        value = value,
        isError = error != null,
        supportingText = {
            if (error != null) {
                Text(
                    color = Color.Red,
                    text = mapError(error.code)
                )
            }
        },
        onValueChange = onValueChange,
        placeholder = { Text(text = placeholder) },
        keyboardOptions = keyboardOptions
    )
}
