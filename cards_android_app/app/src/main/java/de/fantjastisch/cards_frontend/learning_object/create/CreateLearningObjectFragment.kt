package de.fantjastisch.cards_frontend.learning_object.create

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.androidx.AndroidScreen
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.components.ClearFilterButton
import de.fantjastisch.cards_frontend.components.FiltersButton

/**
 * Zeigt den Screen um ein Lernobjekt zu erstellen
 *
 * @author Freja Sender, Semjon Nirmann, Jessica Repty
 */
class CreateLearningObjectFragment : AndroidScreen() {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                    title = { Text(text = stringResource(id = R.string.create_learningobject_headline)) },
                    actions = {
                        FiltersButton()
                        ClearFilterButton()
                    }
                )
            }) {
            CreateLearningObjectView(modifier = Modifier.padding(it))
        }
    }
}