package de.fantjastisch.cards_frontend.infrastructure

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import de.fantjastisch.cards_frontend.util.ErrorsEnum

@Composable
fun ShowErrorOnSignalEffect(
    toast: ErrorsEnum,
    onToastShown: () -> Unit
) {
    val context = LocalContext.current
    val snackbarProvider = SnackbarProvider.current
    LaunchedEffect(
        // wenn sich diese Variable ändert
        key1 = toast,
        // dann wird dieses Lambda ausgeführt.
        block = {
            if (toast != ErrorsEnum.NO_ERROR) {
                snackbarProvider.showSnackbar(
                    message = context.getString(toast.text),
                )
                onToastShown()
            }
        })
}