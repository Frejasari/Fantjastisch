package de.fantjastisch.cards_frontend.learning_overview

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.androidx.AndroidScreen
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.glossary.LearningOverviewModel
import de.fantjastisch.cards_frontend.infrastructure.FantTopBar
import de.fantjastisch.cards_frontend.learning_overview.learning_object_component.LearningObjectComponent

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

@Composable
@Preview
fun LearningOverview(
    modifier: Modifier = Modifier
) {
    val viewModel = viewModel { LearningOverviewModel() }
    LaunchedEffect(
        // wenn sich diese Variable ändert
        key1 = Unit,
        // dann wird dieses Lambda ausgeführt.
        block = {
            viewModel.onPageLoaded()
        })
    // Ein RecyclerView -> Eine lange liste von Eintraegen
    if (viewModel.learningObjects.value.isEmpty()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
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
                LearningObjectComponent(
                    learningObject = learningObject,
                    onDeleteSuccessful = viewModel::onPageLoaded
                )
            }
        }
    }
}

