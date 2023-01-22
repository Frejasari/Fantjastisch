package de.fantjastisch.cards_frontend.link

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import java.util.*

data class LinkSelectItem(val id: UUID,val name : String, val source: UUID, val target: UUID)

@Composable
fun LinkSelect(
    modifier: Modifier = Modifier,
    links: List<LinkSelectItem>,
    onLinkSelected: (UUID) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        links.map { link ->
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .weight(1f),
                    text = link.name
                )
            }
        }
    }
}