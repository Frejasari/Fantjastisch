package de.fantjastisch.cards_frontend.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAltOff
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import de.fantjastisch.cards_frontend.glossary.CardsFilters
import de.fantjastisch.cards_frontend.glossary.filter.GlossaryFilterView
import de.fantjastisch.cards_frontend.infrastructure.FantTabNavigator
import de.fantjastisch.cards_frontend.infrastructure.GlossaryTab
import java.util.*

/**
 * Stellt die TopBar, welche Überschriften, den Hinzufügen-Button, und weitere Elemente enthält,
 * bereit.
 *
 * @author Freja Sender
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FantTopBar(
) {
    val tabNavigator = FantTabNavigator.current
    TopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        title = { Text(text = tabNavigator.current.options.title) },
        actions = {

            AnimatedVisibility(visible = tabNavigator.current == GlossaryTab) {
                ClearFilterButton()
            }
            AnimatedVisibility(visible = tabNavigator.current == GlossaryTab) {
                FiltersButton()
            }
            TobBarCreateMenu()
        }
    )
}

@Composable
fun FiltersButton() {
    val bottomSheetNavigator = LocalBottomSheetNavigator.current
    val hasFilters by CardsFilters.hasFilters.collectAsState(initial = false)
    IconButton(
        onClick = {
            bottomSheetNavigator.show(
                GlossaryFilterView(bottomSheetNavigator = bottomSheetNavigator)
            )
        }
    ) {
        Box {
            Icon(imageVector = Icons.Default.Tune, contentDescription = "more")
            if (hasFilters) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(10.dp)
                        .background(
                            color = MaterialTheme.colorScheme.tertiaryContainer,
                            shape = RoundedCornerShape(50)
                        )
                )
            }
        }
    }
}

@Composable
fun ClearFilterButton() {
    val hasFilters by CardsFilters.hasFilters.collectAsState(initial = false)
    if (hasFilters) {
        IconButton(
            onClick = {
                CardsFilters.reset()
            }
        ) {
            Icon(
                imageVector = Icons.Default.FilterAltOff,
                contentDescription = "clear filters"
            )
        }
    }
}