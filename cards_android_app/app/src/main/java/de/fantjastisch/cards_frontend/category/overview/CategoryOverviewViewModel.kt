package de.fantjastisch.cards_frontend.category.overview

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.card.update.UpdateCardView
import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import kotlinx.coroutines.launch
import org.openapitools.client.models.CategoryEntity

/**
 * Stellt die Daten für die [CategoryOverviewView] bereit und nimmt seine Anfragen entgegen.
 *
 * @property categoryGraphModel Das zugehörige Model, welches die Logik kapselt.
 *
 * @author Tamari Bayer, Freja Sender
 */
class CategoryOverviewViewModel(
    private val categoryGraphModel: CategoryOverviewModel = CategoryOverviewModel()
) : ViewModel() {

    val categories = mutableStateOf<List<CategoryEntity>>(emptyList())
    val error = mutableStateOf<String?>(null)
    val currentDeleteDialog = mutableStateOf<DeletionProgress?>(null)

    /**
     * Holt alle Kategorien ein, indem Sie diese beim [categoryGraphModel] anfragt.
     *
     */
    fun onPageLoaded() {
        viewModelScope.launch {
            when (val result = categoryGraphModel.getCategories()) {
                is RepoResult.Success -> {
                    categories.value = result.result
                }
                is RepoResult.Error -> Unit
                is RepoResult.ServerError -> Unit
            }
        }
    }

    /**
     * Wenn Kategorie gelöscht werden soll -> Dialog öffnen, zum Bestätigen des Löschens.
     *
     * @param cat Kategorie, welche gelöscht werden soll.
     */
    fun onTryDeleteCategory(cat: CategoryEntity) {
        currentDeleteDialog.value = DeletionProgress.ConfirmWithUser(cat)
    }

    /**
     * Kategorie soll gelöscht werden -> [categoryGraphModel] löscht die Kategorie, indem Sie die
     * Anfrage weitergibt.
     *
     */
    fun onDeleteCategoryClicked() {
        val cat = currentDeleteDialog.value!!.cat
        currentDeleteDialog.value = DeletionProgress.Deleting(cat)
        error.value = null
        viewModelScope.launch {
            val result = categoryGraphModel.deleteCategory(
                id = cat.id
            )
            when (result) {
                is RepoResult.Success -> {
                    onPageLoaded()
                    currentDeleteDialog.value = null
                }
                is RepoResult.Error,
                is RepoResult.ServerError -> {
                    // Fehler anzeigen:
                    error.value = "Ein Netzwerkfehler ist aufgetreten."
                }
            }
        }
    }

    /**
     * Kategorie Löschvorgang wurde durch Nutzer abgebrochen. Dialogfenster schließen.
     *
     */
    fun onDeleteCategoryAborted() {
        currentDeleteDialog.value = null
    }

    /**
     * Dialog Fenster für das Bestätigen des Löschens einer Kategorie.
     *
     */
    sealed class DeletionProgress {
        abstract val cat: CategoryEntity

        data class ConfirmWithUser(override val cat: CategoryEntity) : DeletionProgress()
        data class Deleting(override val cat: CategoryEntity) : DeletionProgress()
    }
}