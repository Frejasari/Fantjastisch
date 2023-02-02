package de.fantjastisch.cards_frontend.card.update

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.androidx.AndroidScreen
import de.fantjastisch.cards.R
import java.util.*

/**
 * Zeigt den Screen um eine Karte zu bearbeiten
 *
 * @author Tamari Bayer, Freja Sender
 */
data class UpdateCardFragment(val id: UUID) : AndroidScreen() {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        Scaffold(topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                title = { Text(text = stringResource(id = R.string.update_card_headline)) },
            )
        })
        {
            UpdateCardView(
                modifier = Modifier.padding(it),
                id = id
            )
        }
    }
}
