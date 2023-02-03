package de.fantjastisch.cards_frontend.util

import androidx.compose.runtime.Composable

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