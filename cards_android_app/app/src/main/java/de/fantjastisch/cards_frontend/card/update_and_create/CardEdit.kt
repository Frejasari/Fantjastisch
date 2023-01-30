package de.fantjastisch.cards_frontend.card.update_and_create

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.card.CardSelect
import de.fantjastisch.cards_frontend.card.CardSelectItem
import de.fantjastisch.cards_frontend.card.update.TextFieldState
import de.fantjastisch.cards_frontend.category.CategorySelect
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import de.fantjastisch.cards_frontend.components.OutlinedTextFieldWithErrors
import org.openapitools.client.models.LinkEntity
import java.util.*
import kotlin.reflect.KFunction1


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardEdit(
    modifier: Modifier = Modifier,
    question: TextFieldState,
    answer: TextFieldState,
    tag: TextFieldState,
    categories: List<CategorySelectItem>,
    linkName: TextFieldState,
    cards: List<CardSelectItem>,
    links: List<LinkEntity>,
    onCategorySelected: (UUID) -> Unit,
    onUpdateCardClicked: () -> Unit,
    onCardSelected: (UUID) -> Unit,
    onCreateLinkClicked: () -> Unit,
    onDeleteLinkClicked: (LinkEntity) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var expandedForCat by remember { mutableStateOf(true) }
    val rotate by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f
    )
    val rotateForCat by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f
    )

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        OutlinedTextFieldWithErrors(
            maxLines = 3,
            value = question.value,
            errors = question.errors,
            onValueChange = question.onValueChange,
            placeholder = stringResource(id = R.string.question_label),
            field = "question"
        )
        OutlinedTextFieldWithErrors(
            maxLines = 5,
            value = answer.value,
            errors = answer.errors,
            onValueChange = answer.onValueChange,
            placeholder = stringResource(id = R.string.answer_label),
            field = "answer"
        )
        OutlinedTextFieldWithErrors(
            maxLines = 1,
            value = tag.value,
            errors = tag.errors,
            onValueChange = tag.onValueChange,
            placeholder = stringResource(id = R.string.tag_label),
            field = "tag"
        )

        Divider()
        Row(
            //modifier = Modifier.clickable(onClick = { expanded = !expanded }),
            verticalAlignment = Alignment.CenterVertically

        ) {
            Text(
                text = "Kategorien",
                modifier = Modifier.weight(4.25f)
            )
            IconButton(
                modifier = Modifier
                    .rotate(rotateForCat),
                onClick = {
                    expandedForCat = !expandedForCat
                    if(expanded) {
                        expanded = false
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "drop-down arrow"
                )
            }
        }
        if(expandedForCat) {
            CategorySelect(
                categories = categories,
                onCategorySelected = onCategorySelected
            )
        }


        Divider()
        Row(
            //modifier = Modifier.clickable(onClick = { expanded = !expanded }),
            verticalAlignment = Alignment.CenterVertically

        ) {
            Text(
                text = "Karte verlinken",
                modifier = Modifier.weight(4.25f)
            )
            IconButton(
                enabled = expanded,
                onClick = onCreateLinkClicked
                ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "add link"
                )
            }
            IconButton(
                modifier = Modifier
                    .rotate(rotate),
                onClick = {
                    expanded = !expanded
                    if(expandedForCat) {
                        expandedForCat = false
                    }
                }) {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "drop-down arrow"
                )
            }
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                links.forEach {
                    AssistChip(
                        modifier = Modifier.padding(10.dp),
                        onClick = { },
                        label = {
                            Text(
                                modifier = Modifier,
                                text = it.label!!
                            )
                        },
                        //TODO -> padding anpassen
                        trailingIcon = {
                            IconButton(
                                modifier = Modifier,
                                onClick = { onDeleteLinkClicked(it) }) {
                                Icon(
                                    imageVector = Icons.Default.DeleteOutline,
                                    contentDescription = "delete",
                                    Modifier.size(AssistChipDefaults.IconSize)
                                )
                            }
                        })
                }
            }
        }


        if (expanded) {
            OutlinedTextFieldWithErrors(
                maxLines = 1,
                value = linkName.value,
                errors = linkName.errors,
                onValueChange = linkName.onValueChange,
                placeholder = stringResource(id = R.string.create_link_name),
                field = "name"
            )
                    LazyColumn(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.background),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {

                        CardSelect(
                            cards = cards,
                            onCardSelected = onCardSelected,
                        )
                    }

        }
        FilledTonalButton(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = onUpdateCardClicked
        ) {
            Text(text = stringResource(R.string.save_button_text))
        }

    }
}



