package de.fantjastisch.cards_frontend.card

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.util.formatToInlineLabel
import org.openapitools.client.models.CardEntity

val singleLine = 1
val maximumMultiLines = 10

@Composable
fun CommonCardComponent(
    card: CardEntity
) {
    val maxLines = remember { mutableStateOf(singleLine) }

    Card(
        modifier = Modifier,
        shape = CardDefaults.elevatedShape,
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.elevatedCardColors(
            contentColor = MaterialTheme.colorScheme.onBackground,
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp),

            ) {
            Column() {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                            .weight(weight = 1f, fill = false),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        text = card.question,
                        fontWeight = FontWeight(600)
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier,
                        text = stringResource(R.string.tag_label).formatToInlineLabel(),
                        fontWeight = FontWeight(500),
                        fontSize = 12.sp,
                    )
                    Text(
                        modifier = Modifier,
                        text = card.tag,
                        fontSize = 12.sp,
                    )
                }
            }
            Divider(
                modifier = Modifier
                    .padding(vertical = 6.dp)
            )
            Row(
                Modifier
                    .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                var expanded by remember { mutableStateOf(false) }
                Box(Modifier.clickable(onClick = {
                    switchBetweenSingleLineAndMultiLineCategories(
                        maxLines
                    )
                    expanded = !expanded
                })) {
                    Row(
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = stringResource(R.string.categories_label).formatToInlineLabel(),
                            fontWeight = FontWeight(500),
                            fontSize = 12.sp
                        )
                        var hasMultipleLines by remember { mutableStateOf(false) }
                        Text(
                            text = card.categories.map { category -> category.label }
                                .joinToString(separator = ", "),
                            onTextLayout = {textLayoutResult: TextLayoutResult ->
                                    hasMultipleLines = textLayoutResult.hasVisualOverflow
                                },
                            overflow = TextOverflow.Ellipsis,
                            maxLines = maxLines.value,
                            fontSize = 12.sp
                        )

                        val rotate by animateFloatAsState(
                            targetValue = if (expanded) 180f else 0f
                        )
                        if (hasMultipleLines || maxLines.value == 10) {
                            Icon(
                                modifier = Modifier
                                    .weight(1f)
                                    .rotate(rotate),
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "drop-down arrow"
                            )
                        }
                    }

                }

            }
        }
    }
}

fun switchBetweenSingleLineAndMultiLineCategories(maxLines: MutableState<Int>) {
    if (maxLines.value == singleLine) maxLines.value = maximumMultiLines else maxLines.value =
        singleLine
}