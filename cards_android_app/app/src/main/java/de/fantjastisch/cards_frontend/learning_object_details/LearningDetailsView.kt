package de.fantjastisch.cards_frontend.learning_object_details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.ScreenKey
import de.fantjastisch.cards_frontend.infrastructure.FantMainNavigator
import java.util.*

/**
 * Rendert die Seite Lernobjektansicht.
 *
 * @param modifier Modifier für die Seite.
 * @param learningObjectId Die UUID des Lernobjekts, welches geöffnet werden soll.
 * @param screenKey TODO
 */
@Composable
fun LearningDetailsView(
    modifier: Modifier = Modifier,
    learningObjectId: UUID,
    screenKey: ScreenKey,
) {
    val viewModel =
        viewModel(key = learningObjectId.toString()) { LearningDetailsViewModel(learningObjectId) }

    val navigator = FantMainNavigator.current
    LaunchedEffect(navigator.items) {
        // screen wurde gepoppt -> items hat sich veraendert, dieser effekt wird neu getriggert
        if (navigator.lastItem.key == screenKey) {
            viewModel.onPageLoaded()
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(viewModel.learningBoxes.value) { learningBox ->
            LearningDetailsComponent(
                learningBox = learningBox,
                learningObjectId = learningObjectId
            )
        }
    }
}




