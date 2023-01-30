package de.fantjastisch.cards_frontend.card.update_and_create

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.card.CardSelect
import de.fantjastisch.cards_frontend.card.CardSelectItem
import de.fantjastisch.cards_frontend.card.update.TextFieldState
import de.fantjastisch.cards_frontend.category.CategorySelect
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import de.fantjastisch.cards_frontend.components.OutlinedTextFieldWithErrors
import java.util.*


@Composable
fun CardEdit(
    modifier: Modifier = Modifier,
    question: TextFieldState,
    answer: TextFieldState,
    tag: TextFieldState,
    categories: List<CategorySelectItem>,
    linkName: TextFieldState,
    cards: List<CardSelectItem>,
    onCategorySelected: (UUID) -> Unit,
    onUpdateCardClicked: () -> Unit,
    onCardSelected: (UUID) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

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
       /* OutlinedTextFieldWithErrors(
            maxLines = 1,
            value = linkName.value,
            errors = linkName.errors,
            onValueChange = linkName.onValueChange,
            placeholder = stringResource(id = R.string.link_name),
            field = "linkName"
        )
        LazyColumn(
            modifier
                .background(MaterialTheme.colorScheme.background)
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp))
        {
            CardSelect(
                cards = cards,
                onCardSelected = onCardSelected
            )
        } */
        CategorySelect(
            modifier = Modifier.weight(1f),
            categories = categories,
            onCategorySelected = onCategorySelected
        )


        Divider()
        Row(
            modifier = Modifier.clickable( onClick = {expanded = !expanded}) ,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Karte verlinken")

        }
        if(expanded) {
            OutlinedTextFieldWithErrors(
                maxLines = 1,
                value = linkName.value,
                errors = linkName.errors,
                onValueChange = linkName.onValueChange,
                placeholder = stringResource(id = R.string.create_link_name),
                field = "linkName"
            )
            LazyColumn(
                modifier
                    .background(MaterialTheme.colorScheme.background)
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp))
            {
                CardSelect(
                    cards = cards,
                    onCardSelected = onCardSelected
                )
            }
        }


        FilledTonalButton(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = onUpdateCardClicked
        ) {
            Text(text = stringResource(R.string.save_button_text))
        }
//        LinkEdit(
//            modifier = modifier,
//            name = TextFieldState(
//                value = viewModel.linkName.value,
//                errors = viewModel.errors.value,
//                onValueChange = viewModel::setLinkName,
//            ),
//            cards = viewModel.linkCards.value,
//            onCardSelected = viewModel::onCardSelected,
//            onUpdateLinkClicked = viewModel::onCreateLinkClicked,
//        )
//        CloseScreenOnSignalEffect(shouldClose = viewModel.isFinished.value)
//    }

    }
}