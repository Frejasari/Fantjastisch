package de.fantjastisch.cards_frontend.category

import de.fantjastisch.cards_frontend.category.CategoryRepository
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import org.openapitools.client.models.UpdateCardEntity
import java.util.*

class UpdateCategoryViewModel(
    id: UUID,
    private val categoryRepository: CategoryRepository = CategoryRepository()
) : CategoryViewModel(
    id = id,
   // cardRepository = cardRepository,
    categoryRepository = categoryRepository
) {

    init {
        categoryRepository.getCategory(id = id,
            onSuccess = {
                errors.value = emptyList()
                catId.value = it.id
                catLabel.value = it.label
                //subcategories.value = it.subCategories
                if (subcategories.value?.isEmpty() == true) {
                    subcategories.value = it.subCategories.map { cat ->
                        CategorySelectItem(id = cat, isChecked = true)
                    }
                } else {
                    subcategories.value = subcategories.value.map { category ->
                        if (it.subCategories.firstOrNull { cat -> category.id == id } != null) {
                            CategorySelectItem(
                                label = category.label,
                                id = category.id,
                                isChecked = true
                            )
                        } else {
                            category
                        }
                    }
                }
            },
            onFailure = {
                error.value = "Check network connection"
            })
    }

    override fun save() {
        errors.value = emptyList()
        cardRepository.updateCard(
            card = UpdateCardEntity(
                id = cardId.value!!,
                question = cardQuestion.value,
                answer = cardAnswer.value,
                tag = cardTag.value,
                categories = cardCategories.value.filter { it.isChecked }
                    .map { it.id }//cardCategories.value,
            ),
            onSuccess = {
                isFinished.value = true

                // on Success -> dialog schliessen, zur Card  übersicht?
            },
            onFailure = {
                if (it == null) {
                    error.value = "Irgendwas ist schief gelaufen"
                } else {
                    errors.value = it.errors
                }
                // Fehler anzeigen:
                error.value = "There is an error"
            }
        )
    }

}

