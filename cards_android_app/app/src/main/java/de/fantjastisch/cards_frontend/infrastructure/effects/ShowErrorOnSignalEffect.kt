package de.fantjastisch.cards_frontend.infrastructure.effects

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import de.fantjastisch.cards_frontend.card.update_and_create.ErrorHandlingViewModel
import de.fantjastisch.cards_frontend.infrastructure.SnackbarProvider
import de.fantjastisch.cards_frontend.util.ErrorsEnum


/**
 * Component, welche eine Snackbar anzeigt, wenn ein Fehler auftaucht.
 *
 * @param viewModel das [ErrorHandlingViewModel], welches die Fehler hält.
 *
 * @author Freja Sender
 */
@Composable
fun ShowErrorOnSignalEffect(
    viewModel: ErrorHandlingViewModel
) {
    val context = LocalContext.current
    val snackbarProvider = SnackbarProvider.current
    LaunchedEffect(
        // wenn sich diese Variable ändert
        key1 = viewModel.error.value,
        // dann wird dieses Lambda ausgeführt.
        block = {
            if (viewModel.error.value != ErrorsEnum.NO_ERROR) {
                snackbarProvider.showSnackbar(
                    message = context.getString(viewModel.error.value.text),
                )
                viewModel.onToastShown()
            }
        })
}