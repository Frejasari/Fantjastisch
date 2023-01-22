package de.fantjastisch.cards_frontend.learning_object_details.cards_view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import de.fantjastisch.cards_frontend.infrastructure.FantMainNavigator
import de.fantjastisch.cards_frontend.learning_object_details.cards_view.edit_cards_in_box.EditCardsInBoxFragment
import de.fantjastisch.cards_frontend.learning_object_details.cards_view.move_cards_to_box.MoveCardsToBoxFragment
import java.util.*


@Composable
fun CardsInBoxContextMenu(learningBoxId: UUID, learningObjectId: UUID) {
    val navigator = FantMainNavigator.current
    val isMenuOpen = remember { mutableStateOf(false) }

    IconButton(onClick = { isMenuOpen.value = !isMenuOpen.value }) {
        Icon(Icons.Outlined.MoreVert, contentDescription = "context actions")

        DropdownMenu(
            expanded = isMenuOpen.value,
            onDismissRequest = { isMenuOpen.value = false }
        ) {
            DropdownMenuItem(
                text = { Text(text = "Karten auswählen") },
                onClick = {
                    isMenuOpen.value = false
                    navigator.push(
                        EditCardsInBoxFragment(
                            learningBoxId = learningBoxId,
                            navigator = navigator,
                            learningObjectId = learningObjectId
                        )
                    )
                })
            DropdownMenuItem(
                text = { Text(text = "Karten verschieben") },
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