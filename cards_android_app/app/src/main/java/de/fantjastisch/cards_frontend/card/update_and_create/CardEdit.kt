package de.fantjastisch.cards_frontend.card.update_and_create

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.fantjastisch.cards.R
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
    onCategorySelected: (UUID) -> Unit,
    onUpdateCardClicked: () -> Unit,
) {
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
            placeholder = stringResource(id = R.string.create_card_question_text),
            field = "question"
        )
        OutlinedTextFieldWithErrors(
            maxLines = 5,
            value = answer.value,
            errors = answer.errors,
            onValueChange = answer.onValueChange,
            placeholder = stringResource(id = R.string.create_card_answer_text),
            field = "answer"
        )
        OutlinedTextFieldWithErrors(
            maxLines = 1,
            value = tag.value,
            errors = tag.errors,
            onValueChange = tag.onValueChange,
            placeholder = stringResource(id = R.string.create_card_tag_text),
            field = "tag"
        )
        CategorySelect(
            modifier = Modifier.weight(1f),
            categories = categories,
            onCategorySelected = onCategorySelected
        )
        FilledTonalButton(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = onUpdateCardClicked
        ) {
            Text(text = stringResource(R.string.create_card_save_button_text))
        }
    }
}