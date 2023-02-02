package de.fantjastisch.cards_frontend.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate


@Composable
fun ExpandableRow(
    expanded: Boolean,
    onClick: () -> Unit,
    headline: String,
    additionalActions: @Composable () -> Unit = {},
    additionalContent: @Composable () -> Unit = {},
    expandedContent: @Composable () -> Unit
) {
    val rotate by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f
    )

    Row(
        modifier = Modifier
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            )
            .clickable {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = headline,
            modifier = Modifier.weight(4.25f)
        )
        additionalActions()
        IconButton(
            modifier = Modifier
                .rotate(rotate),
            onClick = onClick
        ) {
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "drop-down arrow"
            )
        }
    }
    additionalContent()
    if (expanded) {
        expandedContent()
    }
}
