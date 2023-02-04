package de.fantjastisch.cards_frontend.category.create

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.card.update_and_create.ErrorHandlingViewModel
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import de.fantjastisch.cards_frontend.util.ErrorsEnum
import de.fantjastisch.cards_frontend.util.RepoResult
import de.fantjastisch.cards_frontend.util.fold
import kotlinx.coroutines.launch
import java.util.*

/**
 * Stellt die Daten für die [CreateCategoryView] bereit und nimmt seine Anfragen entgegen.
 *
 * @property createCategoryModel Das zugehörige Model, welches die Logik kapselt.
 *
 * @author Tamari Bayer, Semjon Nirmann, Freja Sender
 */
class CreateCategoryViewModel(
    private val createCategoryModel: CreateCategoryModel = CreateCategoryModel()
) : ErrorHandlingViewModel() {

    // states, die vom View gelesen werden können -> automatisches Update vom View.
    val isFinished = mutableStateOf(false)
    val label = mutableStateOf("")
    val allCategories = mutableStateOf(listOf<CategorySelectItem>())

    init {
        viewModelScope.launch {

            when (val result = createCategoryModel.getCategories()) {
                is RepoResult.Success -> allCategories.value = result.result
                is RepoResult.Error -> setValidationErrors(result.errors)
                is RepoResult.ServerError -> setUnexpectedError()
            }
        }
    }

    /**
     * Speichert das übergebene Label der Katgegorie in [label].
     *
     * @param value Name der Kategorie.
     */
    fun setLabel(value: String) {
        label.value = value
    }

    /**
     * Speichert die ausgewählten Unterkategorien als isChecked = true in [allCategories]
     *
     * @param id Id der Kategorie, welche neu ausgewählt wurde.
     */
    fun onCategorySelected(id: UUID) {
        allCategories.value = allCategories.value.map {
            if (it.id == id) {
                it.copy(isChecked = !it.isChecked)
            } else {
                it
            }
        }
    }

    /**
     * Wenn Kategorie gespeichert wird -> [CreateCategoryModel] erstellt die Kategorie mit den in den Variablen
     * gespeicherten Daten, indem Sie die Anfrage weiterleitet.
     *
     */
    fun onCreateCategoryClicked() {
        error.value = ErrorsEnum.NO_ERROR
        errors.value = emptyList()

        viewModelScope.launch {
            val result = createCategoryModel.createCategory(
                label = label.value.trim(),
                subCategories = allCategories.value,
            ).fold(
                onSuccess = { isFinished.value = true },
                onValidationError = ::setValidationErrors,
                onUnexpectedError = ::setUnexpectedError
            )
        }
    }
}