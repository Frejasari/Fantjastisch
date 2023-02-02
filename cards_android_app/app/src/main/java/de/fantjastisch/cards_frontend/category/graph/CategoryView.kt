package de.fantjastisch.cards_frontend.category.graph

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
    category: CategoryEntity
) {
    val viewModel = viewModel { CategoryGraphViewModel() }
    var expanded by remember { mutableStateOf(false) }

    FantMainNavigator.current
    val context = LocalContext.current

    ExpandableCard(onClick = { expanded = !expanded }) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(5f),
                    text = category.label,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                CategoryContextMenu(
                    id = category.id,
                    onDeleteClicked = {
                        if (category.subCategories.isNotEmpty()) {
                            Toast.makeText(
                                context,
                                R.string.categories_delete_error,
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            viewModel.onTryDeleteCategory(category)
                        }
                    }
                )
            }

            if (expanded) {
                ExpandedCategoryView(category)
            }
        }
    }
}
