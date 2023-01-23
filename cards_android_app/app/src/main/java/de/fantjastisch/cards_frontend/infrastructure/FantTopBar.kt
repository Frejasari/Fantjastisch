package de.fantjastisch.cards_frontend.infrastructure

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.category.CategoryRepository
import de.fantjastisch.cards_frontend.category.CategorySelect
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import de.fantjastisch.cards_frontend.glossary.CardFilters
import de.fantjastisch.cards_frontend.glossary.CardsFilters
import kotlinx.coroutines.launch
import org.openapitools.client.models.CategoryEntity
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FantTopBar(
) {
    val tabNavigator = FantTabNavigator.current
    // remember -> state nicht neu erzeugen, wenn Funktion neu aufgerufen wird.
    val bottomSheetNavigator = LocalBottomSheetNavigator.current
    TopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        title = { Text(text = tabNavigator.current.options.title) },
        actions = {

            AnimatedVisibility(visible = tabNavigator.current == GlossaryTab) {
//                TODO
                IconButton(
                    onClick = {
                        bottomSheetNavigator.show(GlossaryFilterView())
                    }
                ) {
                    Icon(Icons.Default.Tune, contentDescription = "more")
                }
            }

            TobBarCreateMenu()
        }
    )
}

fun List<CategoryEntity>.toCategorySelectItems() = map { cat ->
    CategorySelectItem(
        id = cat.id,
        label = cat.label,
        isChecked = false
    )
}


class GlossaryFilterModel(
    private val categoryRepository: CategoryRepository = CategoryRepository()
) {
    suspend fun getCategories(): RepoResult<List<CategoryEntity>> {
        return categoryRepository.getPage()
    }
}

class GlossaryFilterViewModel(
    val glossaryFilterModel: GlossaryFilterModel = GlossaryFilterModel()
) : ViewModel() {

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
                onValidationError = {},
                onUnexpectedError = {
                    // TODO TOAST
                }
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
    }
}

@OptIn(ExperimentalMaterial3Api::class)
class GlossaryFilterView : AndroidScreen() {
    // remember -> state nicht neu erzeugen, wenn Funktion neu aufgerufen wird.
    @Composable
    override fun Content() {
        val viewModel = viewModel { GlossaryFilterViewModel() }
        Column {

            Text(text = "hallo ich bin die filter view.")
            OutlinedTextField(
                maxLines = 2,
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.search.value,
                onValueChange = viewModel::onSearchInput,
                placeholder = { Text(text = "In Karten suchen") },
            )
            OutlinedTextField(
                maxLines = 2,
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.tag.value,
                onValueChange = viewModel::onTagInput,
                placeholder = { Text(text = "Nach Tag suchen") },
            )
            CategorySelect(
                categories = viewModel.categories.value,
                onCategorySelected = viewModel::onCategorySelected
            )
            FilledTonalButton(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = viewModel::onLoadPageClicked
            ) {
                Text(text = stringResource(R.string.create_card_save_button_text))
            }
        }

    }
}

