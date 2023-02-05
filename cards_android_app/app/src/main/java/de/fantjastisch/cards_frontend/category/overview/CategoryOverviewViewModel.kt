package de.fantjastisch.cards_frontend.category.overview

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.card.update_and_create.ErrorHandlingViewModel
import de.fantjastisch.cards_frontend.util.ErrorsEnum
import de.fantjastisch.cards_frontend.util.RepoResult.*
import de.fantjastisch.cards_frontend.util.fold
import kotlinx.coroutines.launch
import org.openapitools.client.models.CardEntity
import org.openapitools.client.models.CategoryEntity
import org.openapitools.client.models.ErrorEntryEntity
import java.util.*

/**
 * Stellt die Daten für die [CategoryOverviewView] bereit und nimmt seine Anfragen entgegen.
 *
 * @property categoryGraphModel Das zugehörige Model, welches die Logik kapselt.
 *
 * @author Tamari Bayer, Freja Sender
 */
class CategoryOverviewViewModel(
    private val categoryGraphModel: CategoryOverviewModel = CategoryOverviewModel()
) : ErrorHandlingViewModel() {

    val categories = mutableStateOf<List<CategoryEntity>>(emptyList())
    val cards = mutableStateOf<List<CardEntity>>(emptyList())
    val currentDeleteDialog = mutableStateOf<DeletionProgress?>(null)
    val isParentOpen = mutableStateOf(false)
    val manageStateOfCat = mutableStateOf<UUID?>(null)


    /**
     * Holt alle Kategorien ein, indem Sie diese beim [categoryGraphModel] anfragt.
     *
     */
    fun onPageLoaded() {
        viewModelScope.launch {
            categoryGraphModel.getCategories()
                .fold(
                    onSuccess = {
                        categories.value = it
                    }
                )
        }
    }

    /**
     * Kontrolliert das Ausklappen einer Subkategorie
     *
     * @param catId UUID von der Subkategorie, die beim Klicken ausgeklappt werden soll
     */
    fun manageState(catId: UUID?) {
        manageStateOfCat.value = catId
        isParentOpen.value = catId == null
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
        error.value = ErrorsEnum.NO_ERROR
        viewModelScope.launch {
            val result = categoryGraphModel.deleteCategory(
                id = cat.id
            )
            when (result) {
                is Success -> {
                    onPageLoaded()
                    closeDialog()
                }
                is Error -> {
                    errors.value = result.errors
                    val empty = result.errors.map {
                        it.code
                    }.firstOrNull { it == ErrorEntryEntity.Code.cATEGORYNOTEMPTYVIOLATION }
                    if (empty != null) {
                        error.value = ErrorsEnum.CATEGORY_NOT_EMPTY_ERROR
                        closeDialog()
                    } else {
                        setValidationErrors(result.errors)
                    }

                }
                is ServerError -> setUnexpectedError(result.cause)
            }
        }
    }

    private fun closeDialog() {
        currentDeleteDialog.value = null
    }

    /**
     * Kategorie Löschvorgang wurde durch Nutzer abgebrochen. Dialogfenster schließen.
     */
    fun onDeleteCategoryAborted() {
        currentDeleteDialog.value = null
    }

    /**
     * Klasse, die den State für das Dialog Fenster für das Bestätigen des Löschens einer Kategorie hält.
     *
     */
    sealed class DeletionProgress {
        abstract val cat: CategoryEntity

        data class ConfirmWithUser(override val cat: CategoryEntity) : DeletionProgress()
        data class Deleting(override val cat: CategoryEntity) : DeletionProgress()
    }
}