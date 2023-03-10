package de.fantjastisch.cards_frontend.learning_object_details

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.infrastructure.FantMainNavigator
import de.fantjastisch.cards_frontend.learning_object_details.cards_view.edit_cards_in_box.EditCardsInBoxFragment
import de.fantjastisch.cards_frontend.learning_object_details.cards_view.move_cards_to_box.MoveCardsToBoxFragment
import java.util.*

/**
 * Zeigt das Kontext-Menü einer Lernbox
 *
 * @param learningBoxId Die UUID der Lernbox, deren Kontext-Menü angezeigt werden soll.
 * @param learningObjectId Die UUID des Lernobjekts, zu dem die Lernbox gehört.
 * @param onStartLearningClicked Die Funktion, die dann aufgerufen wird, wenn die Box sich in Lernmodus befindet.
 *
 * @author Jessica Repty, Semjon Nirmann, Freja Sender
 */
@Composable
fun LearningDetailsContextMenu(
    learningBoxId: UUID,
    learningObjectId: UUID,
    onStartLearningClicked: () -> Unit,
    hasCards: Boolean
) {
    val navigator = FantMainNavigator.current
    val isMenuOpen = remember { mutableStateOf(false) }
    IconButton(onClick = { isMenuOpen.value = !isMenuOpen.value }) {
        Icon(Icons.Outlined.MoreVert, contentDescription = "context actions")

        DropdownMenu(
            expanded = isMenuOpen.value,
            onDismissRequest = { isMenuOpen.value = false }
        ) {
            if (hasCards) {
                DropdownMenuItem(
                    text = { Text(text = stringResource(R.string.start_learning)) },
                    onClick = {
                        isMenuOpen.value = false
                        onStartLearningClicked()
                    })
            }
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
            if (hasCards) {
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
}