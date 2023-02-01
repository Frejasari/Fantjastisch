package de.fantjastisch.cards_frontend.glossary;

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.fantjastisch.cards_frontend.card.content_overview.CardContentDialog
import org.openapitools.client.models.LinkEntity
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LinkWithoutDeleteComponent(
    link: LinkEntity
) {
    val dialogOpen = remember { mutableStateOf(false) }

    AssistChip(
        modifier = Modifier.padding(10.dp),
        onClick = { dialogOpen.value = true },
        label = {
            Text(
                modifier = Modifier,
                text = link.label!!
            )
        }
    )

    CardContentDialog(
        id = link.target!!,
        isOpen = dialogOpen.value,
        setIsOpen = { dialogOpen.value = it }
    )
}


