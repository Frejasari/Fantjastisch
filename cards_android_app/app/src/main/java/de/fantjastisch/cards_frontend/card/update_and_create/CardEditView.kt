package de.fantjastisch.cards_frontend.card.update_and_create

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.card.CardSelectItem
import de.fantjastisch.cards_frontend.card.update.TextFieldState
import de.fantjastisch.cards_frontend.category.CategorySelect
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import de.fantjastisch.cards_frontend.components.ExpandableRow
import de.fantjastisch.cards_frontend.components.OutlinedTextFieldWithErrors
import de.fantjastisch.cards_frontend.components.SaveLayout
import de.fantjastisch.cards_frontend.infrastructure.ShowErrorOnSignalEffect
import de.fantjastisch.cards_frontend.util.ErrorsEnum
import org.openapitools.client.models.LinkEntity
import java.util.*

/**
 * Zeigt das Fenster um eine Karte zu bearbeiten/erstellen.
 *
 * @param modifier Modifier für die Seite.
 * @param question Aktuelle Frage, der Karte.
 * @param answer Aktuelle Antwort, der Karte.
 * @param tag Aktuelles Schlagwort, der Karte.
 * @param categories Aktuelle Kategorien, der Karte.
 * @param linkName Aktueller Name eines Links, der Karte.
 * @param cards Aktuell selektierte Karten.
 * @param links Aktuelle Links, der Karte.
 * @param onCategorySelected Funktion, mit der eine Kategorie ausgewäht wird.
 * @param onUpdateCardClicked Funktion, mit der eine Karte geupdated wird.
 * @param onCardSelected Funktion, mit der eine Karte ausgewählt wird.
 * @param onCreateLinkClicked Funktion, mit der eine Karte erzeugt wird.
 * @param onDeleteLinkClicked Funktion, mit der ein Link gelöscht wird.
 * @param toast Text von dem Toast
 * @param onToastShown Callback, sobald der Toast angezeigt wurde.
 *
 * @author Freja Sender, Tamari Bayer
 */
@SuppressLint("UnrememberedMutableState")
@Composable
fun CardEditView(
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
    onDeleteLinkClicked: (LinkEntity) -> Unit,
    toast: ErrorsEnum,
    onToastShown: () -> Unit
) {

    var expanded by remember { mutableStateOf(true) }
    var expandedForLinks by remember { mutableStateOf(false) }
    var expandedForCat by remember { mutableStateOf(false) }

    ShowErrorOnSignalEffect(toast, onToastShown)

    SaveLayout(
        onSaveClicked = onUpdateCardClicked,
        modifier = modifier
    ) {
        ExpandableRow(
            expanded = expanded,
            onClick = {
                expanded = !expanded
                expandedForLinks = false
                expandedForCat = false
            },
            headline = stringResource(id = R.string.allgemein_card_label),
        ) {
            GeneralCardEditFieldsView(question, answer, tag)
        }

        Divider()

        ExpandableRow(
            expanded = expandedForCat,
            onClick = {
                expandedForCat = !expandedForCat
                expandedForLinks = false
                expanded = false
            },
            headline = stringResource(id = R.string.categories_label),
        ) {
            CategorySelect(
                categories = categories,
                onCategorySelected = onCategorySelected
            )
        }

        Divider()

        ExpandableRow(
            expanded = expandedForLinks,
            onClick = {
                expandedForLinks = !expandedForLinks
                expandedForCat = false
                expanded = false
            },
            headline = stringResource(id = R.string.create_link),
            additionalActions = {
                IconButton(
                    enabled = expandedForLinks,
                    onClick = onCreateLinkClicked
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "add link"
                    )
                }
            },
            additionalContent = {
                LazyRow(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        links.forEach {
                            LinkEditView(link = it, onDeleteLinkClicked = onDeleteLinkClicked)
                        }
                    }
                }
            })
        { LinkCreate(linkName, cards, onCardSelected) }
    }
}

@Composable
private fun GeneralCardEditFieldsView(
    question: TextFieldState,
    answer: TextFieldState,
    tag: TextFieldState
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
}
