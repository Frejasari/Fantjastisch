package de.fantjastisch.cards_frontend.learning_mode

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times

/**
 * View für eine Karteikarte im Lernmodus.
 *
 * @param content Frage bzw. Antwort der Karteikarte.
 * @param modifier Modifier für die View.
 * @param onClick Callback, wenn Klick auf Karte.
 *
 * @author Jessica Repty, Freja Sender, Semjon Nirmann
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearningModeCardComponent(
    content: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    Card(
        modifier = Modifier
            .height(0.32 * screenHeight),
        shape = CardDefaults.elevatedShape,
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.elevatedCardColors(
            contentColor = MaterialTheme.colorScheme.onBackground,
            containerColor = MaterialTheme.colorScheme.background
        ),
        onClick = onClick
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(10.dp)
                .align(Alignment.CenterHorizontally)
                .verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                modifier = modifier
                    .padding(5.dp)
                    .fillMaxSize(),
                overflow = TextOverflow.Ellipsis,
                text = content,
                textAlign = TextAlign.Center
            )
        }
    }
}
