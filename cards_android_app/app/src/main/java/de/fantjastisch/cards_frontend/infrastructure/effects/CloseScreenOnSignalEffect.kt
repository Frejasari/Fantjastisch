package de.fantjastisch.cards_frontend.infrastructure.effects

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import de.fantjastisch.cards_frontend.infrastructure.FantMainNavigator

/**
 * Component, welche den aktuellen Screen vom MainNavigator popt.
 *
 * @param shouldClose Wenn dies wahr wird, wird der aktuelle Screen gepopt.
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
