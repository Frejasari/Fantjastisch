package de.fantjastisch.cards_frontend.learning_object_details.cards_view.move_cards_to_box

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.androidx.AndroidScreen
import de.fantjastisch.cards.R
import java.util.*

/**
 * Zeigt den Screen zum Verschieben der Karten in die vorherige/nächste Lernbox an.
 *
 * @property learningBoxId Die UUID der Lernbox, deren Karten verschoben werden sollen.
 * @property learningObjectId Die UUID des Lernobjekts, zu dem die Lernbox gehört.
 *
 * @author Jessica Repty, Semjon Nirmann, Freja Sender
 */
data class MoveCardsToBoxFragment(
    val learningBoxId: UUID,
    val learningObjectId: UUID
) :
    AndroidScreen() {

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
                title = { Text(text = stringResource(R.string.move_cards_in_learningobject_text)) },
            )
        })
        {
            MoveCardsToBoxView(
                modifier = Modifier.padding(it),
                viewModel = viewModel {
                    MoveCardsToBoxViewModel(
                        learningBoxId = learningBoxId,
                        learningObjectId = learningObjectId
                    )
                }
            )
        }
    }
}

