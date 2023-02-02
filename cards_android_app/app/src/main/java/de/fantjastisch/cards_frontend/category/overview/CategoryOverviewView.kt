package de.fantjastisch.cards_frontend.category.overview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.fantjastisch.cards_frontend.category.DeleteCategoryDialog

/**
 * Rendert die Category Overview Seite
 *
 * @param modifier Modifier für die Seite.
 *
 * @author Tamari Bayer, Freja Sender
 */
@Composable
fun CategoryOverviewView(modifier: Modifier = Modifier) {

    val viewModel = viewModel { CategoryOverviewViewModel() }

    val deletionProgress = viewModel.currentDeleteDialog.value
    if (deletionProgress != null) {
        DeleteCategoryDialog(
            cat = deletionProgress.cat,
            isDeleteButtonEnabled = deletionProgress is CategoryOverviewViewModel.DeletionProgress.ConfirmWithUser,
            onDismissClicked = { viewModel.onDeleteCategoryAborted() },
            onDeleteClicked = {
                viewModel.onDeleteCategoryClicked()
            }
        )
    }

    // Lädt die Cards neu, wenn wir aus einem anderen Tab wieder hier rein kommen.
    LaunchedEffect(
        // wenn sich diese Variable ändert
        key1 = Unit,
        // dann wird dieses Lambda ausgeführt.
        block = {
            viewModel.onPageLoaded()
        })

    val listState = rememberLazyListState()


    // Ein RecyclerView -> Eine lange liste von Eintraegen
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        state = listState
    ) {
        itemsIndexed(viewModel.categories.value) { _, category ->
            CategoryView(category)
        }
    }
}
