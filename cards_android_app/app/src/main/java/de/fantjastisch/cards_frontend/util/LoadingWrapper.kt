package de.fantjastisch.cards_frontend.util

import androidx.compose.runtime.Composable


/**
 * Entscheidet welche View gezeigt wird
 * Entschieden wird zwischen [LoadingIcon] und der normalen View
 *
 * @param isLoading Gibt an, ob alle nötigen Informationen zur Verfügung stehen.
 * @param Content Der Inhalt der Seite, wenn alle Informationen zur Verfügung stehen.
 */
@Composable
fun LoadingWrapper(
    isLoading: Boolean,
    Content: @Composable () -> Unit
) {
    if (isLoading) {
        LoadingIcon()
    } else {
        Content()
    }
}