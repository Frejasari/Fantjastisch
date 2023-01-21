package de.fantjastisch.cards_frontend.card

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.*

@Composable
fun CommonCardComponent(
    card: CardSelectItem,
    onCardSelected: (UUID) -> Unit = {},
    hasCheckBox: Boolean
) {
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
                    text = card.question
                )
                if (hasCheckBox) {
                    Checkbox(
                        checked = card.isChecked,
                        onCheckedChange = { onCardSelected(card.id) }
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
                Text(
                    modifier = Modifier,
                    text = "Tag: " + card.tag,
                    fontSize = 12.sp,
                )
                Divider(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(0.5.dp),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    modifier = Modifier,
                    text = "Categories: " + card.categories.joinToString(separator = ", "),
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 12.sp
                )
            }
            Row(
                modifier = Modifier.height(4.dp)
            ) {}
        }
    }
    Row(
        modifier = Modifier.height(4.dp)
    ) {}
}