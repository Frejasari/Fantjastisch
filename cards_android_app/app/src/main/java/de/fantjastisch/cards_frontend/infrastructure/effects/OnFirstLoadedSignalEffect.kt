package de.fantjastisch.cards_frontend.infrastructure.effects

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect


/**
 * Component, welche die onPageLoaded Funktion beim ersten Laden der Seite aufruft.
 *
 * @param onPageLoaded Funktion die aufgerufen wird.
 *
 * @author Freja Sender
 */
@Composable
fun OnFirstLoadedSignalEffect(onPageLoaded: () -> Unit) {
    // einmaliger Effekt
    LaunchedEffect(
        // wenn sich diese Variable ändert
        key1 = Unit,
        // dann wird dieses Lambda ausgeführt.
        block = {
            onPageLoaded()
        })
}

