package de.fantjastisch.cards_frontend.learning_mode

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times

/**
 * View für eine Karteikarte im Lernmodus.
 *
 * @param onClick Callback, wenn Klick auf Karte.
 * @param question Die Frage der Karteikarte.
 * @param answer Die Antwort der Karteikarte.
 * @param isAnswer Ein Boolean, der Angibt ob derzeit die Antwort angezeigt wird.
 *
 * @author Jessica Repty, Freja Sender, Semjon Nirmann
 */
@Composable
fun LearningModeCardComponent(
    onClick: () -> Unit,
    question: String,
    answer: String,
    isAnswer: Boolean
) {
    val rotation by animateFloatAsState(
        targetValue = if (isAnswer) 180f else 0f,
        animationSpec = tween(500),
    )

    Box {
        Card(
            onClick = onClick,
            content = question,
            modifier = Modifier.graphicsLayer {
                rotationY = rotation
                alpha = if (rotation in 0f..90f) 1f else 0f
                cameraDistance = 12f * density
            }
        )
        Card(
            onClick = onClick,
            content = answer,
            modifier = Modifier.graphicsLayer {
                rotationY = rotation - 180f
                alpha = if (rotation in 0f..90f) 0f else 1f
                cameraDistance = 12f * density
            }
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun Card(
    modifier: Modifier,
    onClick: () -> Unit,
    content: String
) {

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    Card(
        modifier = modifier
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
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .align(Alignment.CenterHorizontally)
                .verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxSize(),
                overflow = TextOverflow.Ellipsis,
                text = content,
                textAlign = TextAlign.Center
            )
        }
    }
}
