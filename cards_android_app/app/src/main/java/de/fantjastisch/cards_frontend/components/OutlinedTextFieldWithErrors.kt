package de.fantjastisch.cards_frontend.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import de.fantjastisch.cards_frontend.card.mapError
import org.openapitools.client.models.ErrorEntryEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedTextFieldWithErrors(
    maxLines: Int = 1,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    errors: List<ErrorEntryEntity>,
    field: String
) {
    val error = errors.find { it.field == field }
    OutlinedTextField(
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
    )
}