package de.fantjastisch.cards_frontend.learning_overview

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.androidx.AndroidScreen
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.infrastructure.FantTopBar
import de.fantjastisch.cards_frontend.infrastructure.effects.OnFirstLoadedSignalEffect
import de.fantjastisch.cards_frontend.infrastructure.effects.ShowErrorOnSignalEffect
import de.fantjastisch.cards_frontend.learning_overview.learning_object_component.LearningObjectView

/**
 * Zeigt den Screen für alle Lernobjekten
 *
 * @author Freja Sender
 */
@OptIn(ExperimentalMaterial3Api::class)
class LearningOverviewScreen : AndroidScreen() {
    @Composable
    override fun Content() {
        Scaffold(topBar = { FantTopBar() }) {
            LearningOverview(
                modifier = Modifier.padding(it)
            )
        }
    }
}

/**
 * Rendert die Lernobjekt Overview Seite
 *
 * @param modifier Modifier für die Seite
 *
 * @author Semjon Nirmann, Jessica Repty Freja Sender
 */
@Composable
@Preview
fun LearningOverview(
    modifier: Modifier = Modifier
) {
    val viewModel = viewModel { LearningOverviewViewModel() }
    OnFirstLoadedSignalEffect(onPageLoaded = viewModel::onPageLoaded)
    ShowErrorOnSignalEffect(viewModel = viewModel)

    // Ein RecyclerView -> Eine lange liste von Eintraegen
    if (viewModel.learningObjects.value.isEmpty()) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.no_content_text)
            )
        }
    } else {
        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(viewModel.learningObjects.value) { learningObject ->
                LearningObjectView(
                    learningObject = learningObject,
                    onDeleteSuccessful = viewModel::onPageLoaded
                )
            }
        }
    }
}

