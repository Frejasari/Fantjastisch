package de.fantjastisch.cards_frontend.compose

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import de.fantjastisch.categorys_frontend.category.CategoryRepository
import java.util.*

@Composable
fun CategorySelect(
        modifier: Modifier = Modifier,
) {
    val viewModel = viewModel { CategorySelectViewModel() }

    // Ein RecyclerView -> Eine lange liste von Eintraegen
    LazyColumn {
        items(viewModel.categories.value) { category ->
            Row {
                Text(text = category.label)
                Checkbox(checked = category.isChecked,
                        onCheckedChange = { viewModel.onCategorySelected(category.id) })
            }
        }
    }
}

class CategorySelectViewModel(
        private val categoryRepository: CategoryRepository = CategoryRepository(),
) : ViewModel() {

    fun onCategorySelected(id: UUID) {
        val category = categories.value.first { it.id == id }
        category.isChecked = !category.isChecked
    }

    data class Item(val label: String, val id: UUID, var isChecked: Boolean)

    val categories = mutableStateOf(listOf<Item>())
    val errors = mutableStateOf<String?>(null)

    init {
        categoryRepository.getPage(
                onSuccess = {
                    errors.value = null
                    categories.value = it.map { category ->
                        Item(
                                id = category.id!!,
                                label = category.label!!,
                                isChecked = false,
                        )
                    }
                },
                onFailure = {
                    errors.value = "Da ist aber was kaputt gegangen, hihi"
                },
        )
    }
}