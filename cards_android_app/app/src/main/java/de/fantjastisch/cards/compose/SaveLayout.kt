package de.fantjastisch.cards.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SaveLayout(
    innerLayout: @Composable () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize().padding(all = 16.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            innerLayout()
        }
        ElevatedButton(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = onSave
        ) {
            Text("Speichern")
        }
    }
}

@Preview
@Composable
fun SaveLayoutPreview() {
    MaterialTheme {
        SaveLayout(
            innerLayout = { },
            onSave = { },
        )
    }
}