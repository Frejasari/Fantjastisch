package de.fantjastisch.cards_frontend.infrastructure

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect


/**
 * Component, welche den aktuellen Screen vom MainNavigator popt.
 *
 * @param shouldClose Bei Änderung wird der aktuelle Screen gepopt.
 *
 * @author Freja Sender
 */
@Composable
fun CloseScreenOnSignalEffect(shouldClose: Boolean) {
    val navigator = FantMainNavigator.current
    // einmaliger Effekt
    LaunchedEffect(
        // wenn sich diese Variable ändert
        key1 = shouldClose,
        // dann wird dieses Lambda ausgeführt.
        block = {
            if (shouldClose) {
                navigator.pop()
            }
        })
}

