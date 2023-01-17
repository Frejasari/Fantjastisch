package de.fantjastisch.cards_frontend.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.fantjastisch.cards_frontend.infrastructure.CardsAppTheme

//TODO USE!
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
            Text("Speichern")
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