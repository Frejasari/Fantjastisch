package de.fantjastisch.cards_frontend.learning_object_details

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.glossary.CardsFilters
import de.fantjastisch.cards_frontend.infrastructure.FantMainNavigator
import de.fantjastisch.cards_frontend.learning_object_details.cards_view.edit_cards_in_box.EditCardsInBoxFragment
import de.fantjastisch.cards_frontend.learning_object_details.cards_view.move_cards_to_box.MoveCardsToBoxFragment
import java.util.*


@Composable
fun LearningDetailsContextMenu(
    learningBoxId: UUID,
    learningObjectId: UUID
) {
    val navigator = FantMainNavigator.current
    val isMenuOpen = remember { mutableStateOf(false) }
    IconButton(onClick = { isMenuOpen.value = !isMenuOpen.value }) {
        Icon(Icons.Outlined.MoreVert, contentDescription = "context actions")

        DropdownMenu(
            expanded = isMenuOpen.value,
            onDismissRequest = { isMenuOpen.value = false }
        ) {
            DropdownMenuItem(
                text = { Text(text = stringResource(R.string.show_cards_in_learning_box)) },
                onClick = {
                    isMenuOpen.value = false
                    navigator.push(
                        EditCardsInBoxFragment(
                            learningBoxId = learningBoxId,
                            learningObjectId = learningObjectId,
                        )
                    )
                })
            DropdownMenuItem(
                text = { Text(text = stringResource(R.string.move_cards_text)) },
                onClick = {
                    isMenuOpen.value = false
                    navigator.push(
                        MoveCardsToBoxFragment(
                            learningBoxId = learningBoxId,
                            learningObjectId = learningObjectId
                        )
                    )
                })
        }
    }
}