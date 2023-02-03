package de.fantjastisch.cards_frontend.category.overview

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.category.CategoryContextMenu
import org.openapitools.client.models.CategoryEntity


/**
 * Rendert den extra Teil einer Kategorie Karte, welcher versteckt ist, wenn sie zugeklappt ist.
 *
 * @param category die gerenderte Kategorie
 *
 * @author Tamari Bayer, Freja Sender
 */
@Composable
fun ExpandedCategoryView(
    category: CategoryEntity,
) {
    val viewModel = viewModel { CategoryOverviewViewModel() }
    if (category.subCategories.isEmpty()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(6f)
                    .padding(start = 16.dp),
                text = stringResource(R.string.no_subcategories)
            )
        }
    } else {
        category.subCategories.forEach {
            val nameOfSubcategory = remember { mutableStateOf("") }
            nameOfSubcategory.value =
                viewModel.categories.value.filter { category -> category.id == it }
                    .map { category -> category.label }.first()
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable {
                        viewModel.manageState(it)
                    }
                    .padding(10.dp)

            ) {
                Text(
                    modifier = Modifier
                        .weight(6f)
                        .padding(start = 16.dp),
                    text = nameOfSubcategory.value
                )
            }
        }
    }
}