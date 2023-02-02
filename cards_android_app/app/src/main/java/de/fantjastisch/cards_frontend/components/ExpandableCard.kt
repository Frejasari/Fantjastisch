package de.fantjastisch.cards_frontend.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


/**
 * View, die eine ausklappbare Karte rendert
 *
 * @param onClick Callback, bei Click auf die Karte
 * @param content der Content der Karte.
 */
@Composable
fun ExpandableCard(
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {

    Surface(
        shadowElevation = 6.dp,
        modifier = Modifier
            .clickable(onClick = onClick),
    ) {
        Box(
            modifier = Modifier
                .animateContentSize(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearOutSlowInEasing
                    )
                )
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp),
        ) {
            content()
        }
    }
}
