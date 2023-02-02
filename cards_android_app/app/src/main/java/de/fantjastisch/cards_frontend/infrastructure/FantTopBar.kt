package de.fantjastisch.cards_frontend.infrastructure

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import de.fantjastisch.cards_frontend.glossary.filter.GlossaryFilterView
import org.openapitools.client.models.CategoryEntity
import java.util.*

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

            AnimatedVisibility(visible = tabNavigator.current == GlossaryTab) {
//                TODO
                IconButton(
                    onClick = {
                        bottomSheetNavigator.show(GlossaryFilterView(bottomSheetNavigator = bottomSheetNavigator))
                    }
                ) {
                    Icon(Icons.Default.Tune, contentDescription = "more")
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
