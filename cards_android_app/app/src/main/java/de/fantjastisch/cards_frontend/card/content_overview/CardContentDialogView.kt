package de.fantjastisch.cards_frontend.card.content_overview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import de.fantjastisch.cards_frontend.card.ExpandedCardView
import de.fantjastisch.cards_frontend.components.LoadingIcon
import de.fantjastisch.cards_frontend.infrastructure.effects.ShowErrorOnSignalEffect
import java.util.*

/**
 * Zeigt den Card Content in einem Dialog an.
 *
 * @param id id der Karte
 * @param setIsOpen Funktion, mit der der OpenState des Dialogs gesetzt werden kann
 * @param isOpen Ob der Dialog offen ist oder nicht.
 *
 * @author Tamari Bayer, Freja Sender
 */
@Composable
fun CardContentDialogView(
    id: UUID,
    setIsOpen: (isOpen: Boolean) -> Unit,
    isOpen: Boolean
) {
    val viewModel = viewModel(key = id.toString()) { CardContentViewModel(id = id) }

    ShowErrorOnSignalEffect(viewModel)

    if (isOpen) {
        AlertDialog(
            onDismissRequest = { setIsOpen(false) },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
            ),
            text = {
                if (viewModel.card.value != null) {
                    Column(
                        modifier = Modifier.padding(6.dp)
                    ) {
                        ExpandedCardView(
                            card = viewModel.card.value!!
                        )
                    }

                } else {
                    LoadingIcon()
                }
            },
            confirmButton = {},
            dismissButton = {}
        )
    }
}