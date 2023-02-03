package de.fantjastisch.cards_frontend.category.create

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import kotlinx.coroutines.launch
import org.openapitools.client.models.ErrorEntryEntity
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
) : ViewModel() {

    // states, die vom View gelesen werden können -> automatisches Update vom View.
    val errors = mutableStateOf<List<ErrorEntryEntity>>(emptyList())
    val error = mutableStateOf<String?>(null)
    val isFinished = mutableStateOf(false)
    val label = mutableStateOf("")
    val allCats = mutableStateOf(listOf<CategorySelectItem>())


    init {
        viewModelScope.launch {

            val result = createCategoryModel.getCategories()

            if (result == null) {
                error.value = "Ein Netzwerkfehler ist aufgetreten."
            } else {
                errors.value = emptyList()
                allCats.value = result
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
     * Speichert die ausgewählten Unterkategorien als isChecked = true in [allCats]
     *
     * @param id Id der Kategorie, welche neu ausgewählt wurde.
     */
    fun onCategorySelected(id: UUID) {
        allCats.value = allCats.value.map {
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
        error.value = null
        errors.value = emptyList()

        viewModelScope.launch {
            val result = createCategoryModel.createCategory(
                label = label.value,
                subCategories = allCats.value,
            )

            when (result) {
                is RepoResult.Success -> isFinished.value = true
                is RepoResult.Error -> errors.value = result.errors
                is RepoResult.ServerError -> error.value = "Ein Netzwerkfehler ist aufgetreten."
            }
        }
    }
}