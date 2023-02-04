package de.fantjastisch.cards_frontend.learning_object_details

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.androidx.AndroidScreen
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.components.TobBarCreateMenu
import java.util.*

/**
 * Zeigt den Screen zum Einsehen der Inhalte eines Lernobjekts an.
 *
 * @property learningObjectId Die UUID des Lernobjekts, dessen Inhalte angesehen werden.
 *
 * @author Jessica Repty, Semjon Nirmann, Freja Sender
 */
data class LearningDetailsFragment(val learningObjectId: UUID) :
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
                title = { Text(stringResource(R.string.learning_object_detail)) },
                actions = { TobBarCreateMenu() }
            )
        })

        {
            LearningDetailsView(
                learningObjectId = learningObjectId,
                modifier = Modifier.padding(it),
                screenKey = key
            )
        }
    }
}