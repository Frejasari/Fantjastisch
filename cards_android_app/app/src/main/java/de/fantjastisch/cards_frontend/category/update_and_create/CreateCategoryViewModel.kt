package de.fantjastisch.cards_frontend.category.update_and_create

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import de.fantjastisch.cards_frontend.category.CategoryRepository
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import org.openapitools.client.models.CreateCategoryEntity
import org.openapitools.client.models.ErrorEntryEntity
import java.util.*

class CreateCategoryViewModel(
    private val categoryRepository: CategoryRepository = CategoryRepository(),
    // : ViewModel() = extends ViewModel
) : ViewModel() {

    // states, die vom view gelesen werden können -> automatisches Update vom View.
    val categories = mutableStateOf(listOf<CategorySelectItem>())
    val errors = mutableStateOf<List<ErrorEntryEntity>>(emptyList())
    val error = mutableStateOf<String?>(null)
    val isFinished = mutableStateOf(false)

    val categoryLabel = mutableStateOf("")

    // constructor (wird ganz am Anfang aufgerufen)
    init {
        categoryRepository.getPage(
            onSuccess = {
                errors.value = emptyList()
                error.value = null
                categories.value = it.map { category ->
                    CategorySelectItem(
                        id = category.id,
                        label = category.label,
                        isChecked = false,
                    )
                }
            },
            onFailure = {
                error.value = "Da ist aber was kaputt gegangen, hihi"
            },
        )
    }

    fun onCategorySelected(id: UUID) {
        categories.value = categories.value.map {
            if (it.id == id) {
                it.copy(isChecked = !it.isChecked)
            } else {
                it
            }
        }
    }

    fun onAddCategoryClicked() {
        errors.value = emptyList()
        error.value = null
        categoryRepository.createCategory(
            category = CreateCategoryEntity(
                label = categoryLabel.value,
                subCategories = categories.value.filter { it.isChecked }.map { it.id }
            ),
            onSuccess = {
                isFinished.value = true
                // on Success -> dialog schliessen, zur Category  übersicht?
            },
            onFailure = {
                // Fehler anzeigen:
                if (it == null) {
                    // Fehler anzeigen:
                    error.value = "Irgendwas ist schief gelaufen"
                } else {
                    errors.value = it.errors
                }
            }
        )
    }
}