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

    /**
     * Speichert den übergebenen Suchbegriff in [search].
     *
     * @param query der Suchbegriff, wonach es gesucht wird.
     */
    fun onSearchInput(query: String) {
        search.value = query
    }

    /**
     * Speichert das übergebene Schlagwort in [tag].
     *
     * @param query das Schlagwort, wonach es gesucht wird.
     */
    fun onTagInput(query: String) {
        tag.value = query
    }

    /**
     * Speichert, ob die Karten alphabetisch nach Tags sorteirt werden müssen in [sort]
     *
     * @param sort true - die Karten sind alphabetisch nach Tags sortiert
     *             false - die Karten sind nicht sortiert
     */
    fun onSortClicked(sort: Boolean) {
        this.sort.value = sort
    }

    /**
     * Speichert die ausgewählten Kategorien als isChecked = true in [categories].
     *
     * @param categoryId Id der Kategorie, welche neu ausgewählt wurde.
     */
    fun onCategorySelected(categoryId: UUID) {
        categories.value = categories.value.map {
            if (it.id == categoryId) {
                it.copy(isChecked = !it.isChecked)
            } else {
                it
            }
        }
    }

    /**
     * Setzt die Werte für das Filtern/Sortieren aller Karten
     *
     */
    fun onConfirmClicked() {
        CardsFilters.filters.value = CardFilters(
            search = search.value.trim(),
            tag = tag.value.trim(),
            categories = categories.value.filter { it.isChecked }.map { it.id },
            sort = sort.value
        )
        isFinished.value = true
    }
}