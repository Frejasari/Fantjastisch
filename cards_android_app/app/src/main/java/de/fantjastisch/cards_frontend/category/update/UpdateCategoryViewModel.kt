package de.fantjastisch.cards_frontend.category.update

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.card.update_and_create.ErrorHandlingViewModel
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import de.fantjastisch.cards_frontend.util.ErrorsEnum
import de.fantjastisch.cards_frontend.util.fold
import kotlinx.coroutines.launch
import org.openapitools.client.models.ErrorEntryEntity
import java.util.*

/**
 * Stellt die Daten für die [UpdateCategoryView] bereit und nimmt seine Anfragen entgegen.
 *
 * @property categoryModel Das zugehörige Model, welches die Logik kapselt.
 *
 * @param id Id, der zu bearbeitende Kategorie.
 *
 * @author Tamari Bayer, Freja Sender, Semjon Nirmann
 */
class UpdateCategoryViewModel(
    id: UUID,
    private val categoryModel: UpdateCategoryModel = UpdateCategoryModel(id = id)
) : ErrorHandlingViewModel() {

    val isFinished = mutableStateOf(false)

    val label = mutableStateOf("")
    val allCategories = mutableStateOf(listOf<CategorySelectItem>())

    /**
     * Speichert das übergebene Label der Katgegorie in [label].
     *
     * @param value Name der Kategorie.
     */
    fun setLabel(value: String) {
        label.value = value
    }

    init {
        viewModelScope.launch {
            categoryModel
                .initializePage()
                .fold(
                    onSuccess = { category ->
                        errors.value = emptyList()
                        label.value = category.label
                        allCategories.value = category.allCategories
                    }
                )
        }
    }

    /**
     * Wenn Kategorie geupdated wird -> [UpdateCategoryModel] updated die Kategorie mit den in den Variablen
     * gespeicherten Daten, indem Sie die Anfrage weiterleitet.
     *
     */
    fun onUpdateCategoryClicked() {
        errors.value = emptyList()

        viewModelScope.launch {
            categoryModel.update(
                label = label.value.trim(),
                subCategories = allCategories.value,
            ).fold(
                onSuccess = { isFinished.value = true },
                onValidationError = {
                    if (it.firstOrNull { e -> e.code == ErrorEntryEntity.Code.cYCLICSUBCATEGORYRELATIONVIOLATION } != null) {
                        errors.value = it
                        error.value = ErrorsEnum.CYCLIC_CATEGORIE
                    } else {
                        setValidationErrors(it)
                    }
                }
            )
        }
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

}

