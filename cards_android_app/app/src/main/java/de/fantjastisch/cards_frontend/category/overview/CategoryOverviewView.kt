package de.fantjastisch.cards_frontend.category.overview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.fantjastisch.cards_frontend.category.delete.DeleteCategoryDialog
import de.fantjastisch.cards_frontend.infrastructure.effects.OnFirstLoadedSignalEffect
import de.fantjastisch.cards_frontend.infrastructure.effects.ShowErrorOnSignalEffect
import kotlinx.coroutines.launch

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
    // Lädt die Cards neu, wenn wir aus einem anderen Tab wieder hier rein kommen.
    OnFirstLoadedSignalEffect(onPageLoaded = viewModel::onPageLoaded)
    ShowErrorOnSignalEffect(viewModel = viewModel)

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

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Ein RecyclerView -> Eine lange liste von Eintraegen
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        state = listState
    ) {
        itemsIndexed(viewModel.categories.value) { index, category ->
            CategoryView(
                category = category,
                categoryExpanded = viewModel.manageStateOfCat.value != null
                        && category.id == viewModel.manageStateOfCat.value
                        && !viewModel.isParentOpen.value
            ) {
                coroutineScope.launch {
                    listState.animateScrollToItem(index = index, scrollOffset = 1)
                }
            }
        }
    }
}
