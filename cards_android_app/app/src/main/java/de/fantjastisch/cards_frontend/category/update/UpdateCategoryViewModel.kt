package de.fantjastisch.cards_frontend.category.update

import android.annotation.SuppressLint
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.card.CardSelectItem
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import de.fantjastisch.cards_frontend.category.create.CreateCategoryModel
import de.fantjastisch.cards_frontend.category.create.CreateCategoryView
import de.fantjastisch.cards_frontend.infrastructure.fold
import kotlinx.coroutines.launch
import org.openapitools.client.models.CategoryEntity
import org.openapitools.client.models.ErrorEntryEntity
import org.openapitools.client.models.LinkEntity
import java.util.*

/**
 * Stellt die Daten für die [UpdateCategoryView] bereit und nimmt seine Anfragen entgegen.
 *
 * @property categoryModel Das zugehörige Model, welches die Logik kapselt.
 *
 * @param id Id, der zu bearbeitende Kategorie.
 *
 * @author Tamari Bayer, Freja Sender
 */
class UpdateCategoryViewModel(
    id: UUID,
    private val categoryModel: UpdateCategoryModel = UpdateCategoryModel(id = id)
) : ViewModel() {

    val errors = mutableStateOf<List<ErrorEntryEntity>>(emptyList())
    val error = mutableStateOf<String?>(null)
    val isFinished = mutableStateOf(false)

    val label = mutableStateOf("")
    val allCats = mutableStateOf(listOf<CategorySelectItem>())
    val cats = mutableStateOf(listOf<CategorySelectItem>())

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
                    onSuccess = { cat ->
                        errors.value = emptyList()
                        label.value = cat.label
                        allCats.value = cat.allCategories.map { category ->
                            CategorySelectItem(
                                id = category.id,
                                label = category.label,
                                isChecked = cat.subCategories.contains(category.id)
                                //.firstOrNull { subCatOfCat -> subCatOfCat == cat.id } != null
                            )
                        }
                    },
                    onValidationError = { error.value = "Fehler bei der Eingabevalidierung." },
                    onUnexpectedError = { error.value = "Ein unbekannter Fehler ist aufgetreten." },
                )

            val resultCategories = categoryModel.getCategories()

            if (resultCategories == null) {
                error.value = "Ein Netzwerkfehler ist aufgetreten."
            } else {
                errors.value = emptyList()
                cats.value = resultCategories
            }
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
                label = label.value,
                subCategories = allCats.value,
            ).fold(
                onSuccess = { isFinished.value = true },
                onValidationError = { errors.value = it },
                onUnexpectedError = { error.value = "Ein unbekannter Fehler ist aufgetreten." }
            )

        }
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

}

