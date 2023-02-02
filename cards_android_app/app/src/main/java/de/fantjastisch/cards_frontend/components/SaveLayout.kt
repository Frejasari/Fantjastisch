package de.fantjastisch.cards_frontend.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.infrastructure.CardsAppTheme

//TODO USE!
/**
 * View für den Speichern Button.
 *
 * @param innerLayout TODO
 * @param onSave Callback, wenn Click auf Speichern
 * @param modifier Modifier für die View.
 *
 * @author Semjon Nirmann, Freja Sender
 */
@Composable
fun SaveLayout(
    innerLayout: @Composable () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(all = 16.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            innerLayout()
        }
        ElevatedButton(
            modifier = Modifier.fillMaxWidth(), onClick = onSave
        ) {
            Text(text = stringResource(R.string.save_button_text))
        }
    }
}

@Preview
@Composable
fun SaveLayoutPreview() {
    CardsAppTheme {
        SaveLayout(
            innerLayout = { },
            onSave = { },
        )
    }
}