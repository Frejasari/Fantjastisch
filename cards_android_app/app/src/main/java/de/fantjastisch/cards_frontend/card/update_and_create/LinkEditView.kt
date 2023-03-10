package de.fantjastisch.cards_frontend.card.update_and_create

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.fantjastisch.cards_frontend.card.content_overview.CardContentDialogView
import org.openapitools.client.models.LinkEntity
import java.util.*

/**
 * Zeigt den AssistChip eines Links inklusive Delete Option.
 *
 * @param link Link, welcher angezeigt wird.
 * @param onDeleteLinkClicked Funktion, um einen Link zu löschen
 *
 * @author Jessica Repty, Tamari Bayer, Freja Sender
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LinkEditView(
    link: LinkEntity,
    onDeleteLinkClicked: (LinkEntity) -> Unit
) {
    val dialogOpen = remember { mutableStateOf(false) }

    AssistChip(
        modifier = Modifier.padding(6.dp),
        onClick = { dialogOpen.value = true },
        label = {
            Text(
                modifier = Modifier,
                text = link.label
            )
        },
        trailingIcon = {
            IconButton(
                modifier = Modifier,
                onClick = { onDeleteLinkClicked(link) }
            ) {
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = "delete",
                    Modifier.size(AssistChipDefaults.IconSize)
                )
            }
        }
    )

    CardContentDialogView(
        id = link.target,
        isOpen = dialogOpen.value,
        setIsOpen = { dialogOpen.value = it }
    )
}



