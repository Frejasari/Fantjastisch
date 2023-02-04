package de.fantjastisch.cards_frontend.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


/**
 * Snackbar Component, wird gerendert, wenn wir eine Snackbar ausgeben.
 *
 * @param snackbarData vom Framework zur Verfuügung gestellte Daten fuür die Snackbar
 *
 * @author Freja Sender
 */
@Composable
fun CustomSnackBar(snackbarData: SnackbarData) {
    Box(
        modifier = Modifier.padding(24.dp)
    ) {
        Surface(
            color = MaterialTheme.colorScheme.error,
            shape = MaterialTheme.shapes.medium,
            shadowElevation = 6.dp,
        ) {
            Row(
                modifier = Modifier
                    .padding(24.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(Icons.Outlined.ErrorOutline, contentDescription = "error")
                Text(
                    text = snackbarData.visuals.message
                )
            }
        }
    }
}
