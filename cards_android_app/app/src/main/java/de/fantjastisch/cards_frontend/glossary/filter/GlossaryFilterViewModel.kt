package de.fantjastisch.cards_frontend.glossary.filter

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.card.update_and_create.ErrorHandlingViewModel
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import de.fantjastisch.cards_frontend.glossary.CardFilters
import de.fantjastisch.cards_frontend.glossary.CardsFilters
import de.fantjastisch.cards_frontend.infrastructure.fold
import de.fantjastisch.cards_frontend.infrastructure.toCategorySelectItems
import kotlinx.coroutines.launch
import java.util.*

/**
 * Stellt die Daten für die [GlossaryFilterView] bereit und nimmt seine Anfragen entgegen.
 *
 * @property glossaryFilterModel Das zugehörige Model, welches die Logik kapselt.
 *
 * @author Freja Sender
 */
class GlossaryFilterViewModel(
    private val glossaryFilterModel: GlossaryFilterModel = GlossaryFilterModel()
) : ErrorHandlingViewModel() {

    val isFinished = mutableStateOf(false)
    val search = mutableStateOf(CardsFilters.filters.value.search)
    val tag = mutableStateOf(CardsFilters.filters.value.tag)
    val categories = mutableStateOf<List<CategorySelectItem>>(emptyList())
    val sort = mutableStateOf(CardsFilters.filters.value.sort)

    init {
        viewModelScope.launch {
            glossaryFilterModel.getCategories().fold(
                onSuccess = {
                    categories.value = it.toCategorySelectItems()
                        .map { categorySelectItem ->
                            if (CardsFilters.filters.value.categories.contains(categorySelectItem.id)) {
                                CategorySelectItem(
                                    label = categorySelectItem.label,
                                    id = categorySelectItem.id,
                                    isChecked = true
                                )
                            } else {
                                categorySelectItem
                            }
                        }
                },
                onValidationError = ::setValidationErrors,
                onUnexpectedError = ::setUnexpectedErrors,
            )
        }
    }

    fun onSearchInput(query: String) {
        search.value = query
    }

    fun onTagInput(query: String) {
        tag.value = query
    }

    fun onSortClicked(sort: Boolean) {
        this.sort.value = sort
    }

    fun onCategorySelected(categoryId: UUID) {
        categories.value = categories.value.map {
            if (it.id == categoryId) {
                it.copy(isChecked = !it.isChecked)
            } else {
                it
            }
        }
    }

    fun onLoadPageClicked() {
        CardsFilters.filters.value = CardFilters(
            search = search.value,
            tag = tag.value,
            categories = categories.value.filter { it.isChecked }.map { it.id },
            sort = sort.value
        )
        isFinished.value = true
    }
}