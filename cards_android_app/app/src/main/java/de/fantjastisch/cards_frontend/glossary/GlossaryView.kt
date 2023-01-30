package de.fantjastisch.cards_frontend.glossary

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.card.CardContextMenu
import de.fantjastisch.cards_frontend.card.content_overview.CardContentDialog
import de.fantjastisch.cards_frontend.card.content_overview.CardContentViewModel
import de.fantjastisch.cards_frontend.card.delete.DeleteCardDialog
import de.fantjastisch.cards_frontend.glossary.GlossaryViewModel.DeletionProgress
import kotlinx.coroutines.launch
import org.openapitools.client.models.CardEntity
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
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Ein RecyclerView -> Eine lange liste von Eintraegen
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        state = listState
    ) {
        itemsIndexed(viewModel.cards.value) { index, card ->
            CardView(card, viewModel) {
                coroutineScope.launch {
                    // Animate scroll to the 10th item
                    listState.animateScrollToItem(index = index)
                }
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun CardView(
    card: CardEntity,
    viewModel: GlossaryViewModel,
    onItemExpanded: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val rotate by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f
    )

    Surface(
        modifier = Modifier.clickable(
            onClick = {
                expanded = !expanded
                onItemExpanded()
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
                                text = stringResource(R.string.inline_tag_label),
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
                        text = stringResource(R.string.inline_categories_label),
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
                            onItemExpanded()
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
                        text = stringResource(R.string.answer_label),
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
                        text = stringResource(R.string.tag_label),
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
                        text = stringResource(R.string.categories_label),
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
                        text = stringResource(R.string.links_label),
                        fontWeight = FontWeight(300),
                        fontSize = 12.sp
                    )
                }
                LazyRow(
                    modifier = Modifier
                ) {
                    item {
                        card.links.forEach {
                            val viewModel = viewModel { CardContentViewModel(it.target!!) }

                            if (viewModel.linkClicked.value) {
                                CardContentDialog(id = it.target!!)
                            }

                            AssistChip(
                                modifier = Modifier.padding(10.dp),
                                onClick = viewModel::onLinkClicked,
                                label = {
                                    Text(
                                        modifier = Modifier,
                                        text = it.label!!
                                    )
                                },
                                //TODO -> padding anpassen
                                /*trailingIcon = {
                                    IconButton(
                                        modifier = Modifier,
                                        onClick = { onDeleteLinkClicked(it) }) {
                                        Icon(
                                            imageVector = Icons.Default.DeleteOutline,
                                            contentDescription = "delete",
                                            Modifier.size(AssistChipDefaults.IconSize)
                                        )
                                    }
                                }*/ )
                        }
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.End
                ) {
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
                }}



            }
        }
    }
}

