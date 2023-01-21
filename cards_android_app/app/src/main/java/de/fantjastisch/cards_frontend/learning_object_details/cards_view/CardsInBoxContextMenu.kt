package de.fantjastisch.cards_frontend.learning_object_details.cards_view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.navigator.Navigator
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.learning_object_details.cards_view.CardsInBoxFragment
import de.fantjastisch.cards_frontend.learning_object_details.cards_view.edit_cards_in_box.AddCardsToBoxFragment
import java.util.*


@Composable
fun CardsInBoxContextMenu(learningBoxId: UUID, navigator: Navigator) {
    val isMenuOpen = remember { mutableStateOf(false) }

    IconButton(onClick = { isMenuOpen.value = !isMenuOpen.value }) {
        Icon(Icons.Outlined.MoreVert, contentDescription = "context actions")

        DropdownMenu(
            expanded = isMenuOpen.value,
            onDismissRequest = { isMenuOpen.value = false }
        ) {
            DropdownMenuItem(
                text = { Text(text="Karten hinzufügen") },
                onClick = {
                    isMenuOpen.value = false
                    navigator.push(AddCardsToBoxFragment(learningBoxId=learningBoxId, navigator=navigator))
                })
            DropdownMenuItem(
                text = { Text(text="Karten entfernen") },
                onClick = {
                    isMenuOpen.value = false
                    //navigator.push(LearningModeFragment(learningBoxId))
                })
            DropdownMenuItem(
                text = { Text(text="Karten verschieben") },
                onClick = {
                    isMenuOpen.value = false
                    //navigator.push(LearningModeFragment(learningBoxId))
                })
        }
    }
}