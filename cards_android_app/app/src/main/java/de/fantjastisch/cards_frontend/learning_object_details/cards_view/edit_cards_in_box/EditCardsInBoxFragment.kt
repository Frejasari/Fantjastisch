package de.fantjastisch.cards_frontend.learning_object_details.cards_view.edit_cards_in_box

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
 * Zeigt den Screen zum Einsehen der Karten eines Lernobjekts an.
 *
 * @property learningBoxId Die UUID der Lernbox, deren Karten angezeigt werden sollen.
 * @property learningObjectId Die UUID des Lernobjekts, zu dem die Lernbox gehört.
 *
 * @author
 */
data class EditCardsInBoxFragment(
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
                title = { Text(text = stringResource(R.string.cards_in_this_box_label)) }
            )
        })
        {
            EditCardsInBoxView(
                modifier = Modifier.padding(it),
                viewModel = viewModel {
                    EditCardsInBoxViewModel(
                        learningBoxId = learningBoxId,
                        learningObjectId = learningObjectId
                    )
                }
            )
        }
    }
}

