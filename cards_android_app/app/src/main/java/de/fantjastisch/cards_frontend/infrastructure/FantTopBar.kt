package de.fantjastisch.cards_frontend.infrastructure

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
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import de.fantjastisch.cards_frontend.glossary.CardsFilters
import de.fantjastisch.cards_frontend.glossary.filter.GlossaryFilterView
import org.openapitools.client.models.CategoryEntity
import java.util.*

/**
 * TODO
 *
 * @author Freja Sender
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FantTopBar(
) {
    val tabNavigator = FantTabNavigator.current
    // remember -> state nicht neu erzeugen, wenn Funktion neu aufgerufen wird.
    val bottomSheetNavigator = LocalBottomSheetNavigator.current
    TopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        title = { Text(text = tabNavigator.current.options.title) },
        actions = {

            val filters by CardsFilters.filters.collectAsState()
            val hasFilters =
                filters.tag.isNotBlank() || filters.search.isNotBlank() || filters.categories.isNotEmpty()

            AnimatedVisibility(hasFilters) {
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
            AnimatedVisibility(visible = tabNavigator.current == GlossaryTab) {
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
            TobBarCreateMenu()
        }
    )
}

fun List<CategoryEntity>.toCategorySelectItems() = map { cat ->
    CategorySelectItem(
        id = cat.id,
        label = cat.label,
        isChecked = false
    )
}
