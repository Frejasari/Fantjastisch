package de.fantjastisch.cards_frontend.glossary

import androidx.compose.foundation.layout.*
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
 * Zeigt den AssistChip eines Links ohne Delete Option.
 *
 * @param link Link, welcher angezeigt werden soll.
 *
 * @author Jessica Repty, Tamari Bayer, Freja Sender
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LinkWithoutDeleteComponent(
    link: LinkEntity
) {
    val dialogOpen = remember { mutableStateOf(false) }

    AssistChip(
        modifier = Modifier.padding(6.dp),
        onClick = { dialogOpen.value = true },
        label = {
            Text(
                text = link.label
            )
        }
    )

    CardContentDialogView(
        id = link.target,
        isOpen = dialogOpen.value,
        setIsOpen = { dialogOpen.value = it }
    )
}



