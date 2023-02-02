package de.fantjastisch.cards_frontend.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import de.fantjastisch.cards_frontend.util.mapError
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
    modifier: Modifier = Modifier,
    maxLines: Int = 1,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    errors: List<ErrorEntryEntity>,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    field: String,
    trailingIcon: @Composable (() -> Unit)? = null,
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(),
    enabled: Boolean = true
) {
    val error = errors.find { it.field == field }

    OutlinedTextField(
        singleLine = maxLines == 1,
        maxLines = maxLines,
        modifier = modifier.fillMaxWidth(),
        value = value,
        isError = error != null,
        supportingText = {
            if (error != null) {
                Text(
                    color = Color.Red,
                    text = stringResource(id = mapError(error.code))
                )
            }
        },
        onValueChange = onValueChange,
        placeholder = { Text(text = placeholder) },
        label = { Text(text = placeholder) },
        keyboardOptions = keyboardOptions,
        trailingIcon = trailingIcon,
        colors = colors,
        enabled = enabled
    )
}
