package de.fantjastisch.cards_frontend.learning_object_details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.ScreenKey
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.infrastructure.FantMainNavigator
import de.fantjastisch.cards_frontend.infrastructure.effects.ShowErrorOnSignalEffect
import java.util.*

/**
 * Rendert die Seite Lernobjektansicht.
 *
 * @param modifier Modifier für die Seite.
 * @param learningObjectId Die UUID des Lernobjekts, welches geöffnet werden soll.
 * @param screenKey Ein Key (String-Alias) welcher übergeben wird, um das Laden der Seite
 * hervorzurufen.
 *
 * @author Jessica Repty, Semjon Nirmann, Freja Sender
 */
@Composable
fun LearningDetailsView(
    modifier: Modifier = Modifier,
    learningObjectId: UUID,
    screenKey: ScreenKey,
) {
    val viewModel =
        viewModel(key = learningObjectId.toString()) { LearningDetailsViewModel(learningObjectId) }
    ShowErrorOnSignalEffect(viewModel = viewModel)

    val navigator = FantMainNavigator.current
    LaunchedEffect(navigator.items) {
        // screen wurde gepoppt -> items hat sich veraendert, dieser Effekt wird neu getriggert
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

    if (viewModel.celebrate.value) {
        CelebrationAnimation()
    }
}

/**
 * Zeigt das Konfetti an, wenn alle Karten gelernt wurden.
 *
 */
@Composable
fun CelebrationAnimation() {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.party_celebration)
    )
    LottieAnimation(
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.FillHeight,
        composition = composition
    )
}
