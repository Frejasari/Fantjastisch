package de.fantjastisch.cards_frontend.glossary

import android.annotation.SuppressLint
import android.graphics.fonts.FontStyle
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.fantjastisch.cards_frontend.card.CardContentView
import de.fantjastisch.cards_frontend.card.CardContextMenu
import de.fantjastisch.cards_frontend.card.content_overview.CardContentFragment
import de.fantjastisch.cards_frontend.card.delete.DeleteCardDialog
import de.fantjastisch.cards_frontend.glossary.GlossaryViewModel.DeletionProgress
import de.fantjastisch.cards_frontend.infrastructure.FantMainNavigator
import org.openapitools.client.models.CardEntity
import org.openapitools.client.models.LinkEntity
import java.util.*


@Composable
@Preview
fun GlossaryView(
    modifier: Modifier = Modifier
) {

    val viewModel = viewModel { GlossaryViewModel() }

    val deletionProgress = viewModel.currentDeleteDialog.value
    if (deletionProgress != null) {
        DeleteCardDialog(
            card = deletionProgress.card,
            isDeleteButtonEnabled = deletionProgress is DeletionProgress.ConfirmWithUser,
            onDismissClicked = { viewModel.onDeleteCardAborted() },
            onDeleteClicked = {
                viewModel.onDeleteCardClicked()
            }
        )
    }

    // Lädt die Cards neu, wenn wir aus einem anderen Tab wieder hier rein kommen.
    LaunchedEffect(
        // wenn sich diese Variable ändert
        key1 = Unit,
        // dann wird dieses Lambda ausgeführt.
        block = {
            viewModel.onPageLoaded()
        })

    // Ein RecyclerView -> Eine lange liste von Eintraegen
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(viewModel.cards.value) { card ->
            CardView(card, viewModel)
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun CardView(
    card: CardEntity,
    viewModel: GlossaryViewModel
) {
    var expanded by remember { mutableStateOf(false) }
    val rotate by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f
    )

    Surface(
        modifier = Modifier.clickable(
            onClick = {
                expanded = !expanded
            }
        ),
        shadowElevation = 6.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp),

            ) {
            if (!expanded) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .weight(weight = 1f)
                    ) {
                        Text(
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            text = card.question
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier,
                                text = "Tag: ",
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
                    CardContextMenu(
                        cardId = card.id,
                        onDeleteClicked = { viewModel.onTryDeleteCard(card) },
                    )
                }
                Divider(
                    modifier = Modifier
                        .padding(vertical = 6.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Kategorien: ",
                        fontSize = 14.sp
                    )
                    Text(
                        modifier = Modifier.weight(4.25f),
                        text = card.categories.map { category -> category.label }
                            .joinToString(separator = ", "),
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    IconButton(
                        modifier = Modifier
                            .rotate(rotate),
                        onClick = {
                            expanded = !expanded
                        }) {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "drop-down arrow"
                        )
                    }
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .weight(weight = 1f)
                    ) {
                        Text(
                            text = card.question
                        )
                    }
                    CardContextMenu(
                        cardId = card.id,
                        onDeleteClicked = { viewModel.onTryDeleteCard(card) },
                    )
                }
                Divider(
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Antwort",
                        fontWeight = FontWeight(300),
                        fontSize = 12.sp
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = card.answer
                    )
                }
                Divider(
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Tag",
                        fontWeight = FontWeight(300),
                        fontSize = 12.sp
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier,
                        text = card.tag,
                    )
                }
                Divider(
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Kategorien",
                        fontWeight = FontWeight(300),
                        fontSize = 12.sp
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.weight(4.25f),
                        text = card.categories.map { category -> category.label }
                            .joinToString(separator = ", "),
                    )
                }
                Divider(
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Links",
                        fontWeight = FontWeight(300),
                        fontSize = 12.sp
                    )
                    IconButton(
                        modifier = Modifier
                            .rotate(rotate),
                        onClick = {
                            expanded = !expanded
                        }) {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "drop-down arrow"
                        )
                    }
                }
            }
        }
    }
}

