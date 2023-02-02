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

/**
 * View, für ein umrundetes Textfeld mit Fehleranzeige.
 *
 * @param maxLines Anzahl der maximal anzuzeigenden Textzeilen.
 * @param value Text, welcher angezeigt werden soll.
 * @param onValueChange Callback, wenn sich Text ändert.
 * @param placeholder Label, welches angezeigt wird, wenn noch kein Text eingegeben.
 * @param errors Mögliche Liste an Fehlermeldungen.
 * @param keyboardOptions KeyboardOptions für das Textfeld.
 * @param field Kontext des Textfeldes, zum Anzeigen bei Fehlermeldungen.
 *
 * @author Freja Sender, Semjon Nirmann
 */
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
        label = { Text(text = placeholder) },
        keyboardOptions = keyboardOptions
    )
}
