package de.fantjastisch.cards_frontend.category

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import org.openapitools.client.models.CreateCategoryEntity
import java.util.*

class CreateCategoryViewModel(
    private val categoryRepository: CategoryRepository = CategoryRepository(),
    // : ViewModel() = extends ViewModel
) : ViewModel() {

    // states, die vom view gelesen werden können -> automatisches Update vom View.
    val categories = mutableStateOf(listOf<CategorySelectItem>())
    val errors = mutableStateOf<String?>(null)
    val isFinished = mutableStateOf(false)

    val categoryLabel = mutableStateOf("")

    // constructor (wird ganz am Anfang aufgerufen)
    init {
        categoryRepository.getPage(
            onSuccess = {
                errors.value = null
                categories.value = it.map { category ->
                    CategorySelectItem(
                        id = category.id,
                        label = category.label,
                        isChecked = false,
                    )
                }
            },
            onFailure = {
                errors.value = "Da ist aber was kaputt gegangen, hihi"
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
        errors.value = null
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
                errors.value = "There is an error"
            }
        )
    }
}