package de.fantjastisch.cards_frontend.category.overview

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.compose.viewModel
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.category.CategoryContextMenu
import de.fantjastisch.cards_frontend.components.ExpandableCard
import de.fantjastisch.cards_frontend.infrastructure.FantMainNavigator
import org.openapitools.client.models.CategoryEntity

/**
 * Rendert eine Kategorie Karte
 *
 * @param category die angezeigte Kategorie
 *
 * @author Tamari Bayer, Freja Sender
 */
@SuppressLint("UnrememberedMutableState")
@Composable
fun CategoryView(
    category: CategoryEntity,
    categoryExpanded: Boolean,
    onItemExpanded: () -> Unit
) {
    val viewModel = viewModel { CategoryOverviewViewModel() }
    var expanded by remember { mutableStateOf(false) }

    FantMainNavigator.current

    expanded = categoryExpanded && !viewModel.isParentOpen.value


    ExpandableCard(
        onClick = {
            expanded = !expanded
            onItemExpanded()
            viewModel.manageState(null)
        }) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = category.label,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                CategoryContextMenu(
                    id = category.id,
                    onDeleteClicked = { viewModel.onTryDeleteCategory(category)
                    }
                )
            }
            if (expanded) {
                ExpandedCategoryView(category)
                viewModel.isParentOpen.value = false
            }
        }
    }
}


