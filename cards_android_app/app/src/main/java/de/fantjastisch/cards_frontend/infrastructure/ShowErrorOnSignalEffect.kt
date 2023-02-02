package de.fantjastisch.cards_frontend.infrastructure

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext

@Composable
fun ShowErrorOnSignalEffect(
    toast: ErrorTexts,
    onToastShown: () -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(
        // wenn sich diese Variable ändert
        key1 = toast,
        // dann wird dieses Lambda ausgeführt.
        block = {
            if (toast != ErrorTexts.NO_ERROR) {
                Toast.makeText(context, toast.text, Toast.LENGTH_SHORT).show()
                onToastShown()
            }
        })
}