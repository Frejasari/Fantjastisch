package de.fantjastisch.cards_frontend.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import de.fantjastisch.cards.R
import org.openapitools.client.models.CreateCategoryEntity
import java.util.*

//TODO Fehler anzeigen.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCategoryView(
    modifier: Modifier = Modifier,
    fragmentManager: FragmentManager
) {
    val viewModel = viewModel { CreateCategoryViewModel() }

    // Componente die ihre Kinder untereinander anzeigt.
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .fillMaxSize()
    ) {
        OutlinedTextField(
            maxLines = 1,
            keyboardActions = KeyboardActions(
                onDone = { viewModel.onAddCategoryClicked() },
            ),
            modifier = Modifier.fillMaxWidth(),
            value = viewModel.categoryLabel.value,
            onValueChange = { viewModel.categoryLabel.value = it },
            placeholder = { Text(text = stringResource(id = R.string.create_category_label_text)) },
        )

        CategorySelect(
            categories = viewModel.categories.value,
            onCategorySelected = viewModel::onCategorySelected
        )

        FilledTonalButton(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = viewModel::onAddCategoryClicked
        ) {
            Text(text = stringResource(R.string.create_category_save_button_text))
        }
    }

    // einmaliger Effekt
    LaunchedEffect(
        // wenn sich diese Variable ändert
        key1 = viewModel.isFinished.value,
        // dann wird dieses Lambda ausgeführt.
        block = {
            if (viewModel.isFinished.value) {
                fragmentManager.popBackStack()
            }
        })

}

//val = final
//var
data class A(val label: String?, val description: String)

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

class CreateCategoryDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext())
            .apply {
                setContent {
                    CreateCategoryView(
                        fragmentManager = requireActivity().supportFragmentManager
                    )
                }
            }
    }
}