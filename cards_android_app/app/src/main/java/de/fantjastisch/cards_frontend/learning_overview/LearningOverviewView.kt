package de.fantjastisch.cards_frontend.learning_overview

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.infrastructure.FantMainNavigator
import de.fantjastisch.cards_frontend.infrastructure.effects.OnFirstLoadedSignalEffect
import de.fantjastisch.cards_frontend.infrastructure.effects.ShowErrorOnSignalEffect
import de.fantjastisch.cards_frontend.learning_object.create.CreateLearningObjectFragment
import de.fantjastisch.cards_frontend.learning_overview.learning_object_component.LearningObjectView

/**
 * Rendert die Lernobjekt Overview Seite
 *
 * @param modifier Modifier für die Seite
 *
 * @author Semjon Nirmann, Jessica Repty Freja Sender
 */
@Composable
@Preview
fun LearningOverviewView(
    modifier: Modifier = Modifier
) {
    val navigator = FantMainNavigator.current
    val viewModel = viewModel { LearningOverviewViewModel() }
    OnFirstLoadedSignalEffect(onPageLoaded = viewModel::onPageLoaded)
    ShowErrorOnSignalEffect(viewModel = viewModel)

    // Ein RecyclerView -> Eine lange liste von Eintraegen
    if (viewModel.learningObjects.value.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            ElevatedButton(
                modifier = Modifier.aspectRatio(1f),
                shape = RoundedCornerShape(50),
                onClick = { navigator.push(CreateLearningObjectFragment()) }) {
                Text(text = stringResource(R.string.menu_create_learningobject))
            }
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

