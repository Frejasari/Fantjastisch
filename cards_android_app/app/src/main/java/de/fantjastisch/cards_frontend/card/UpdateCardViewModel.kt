package de.fantjastisch.cards_frontend.card

import de.fantjastisch.cards_frontend.category.CategoryRepository
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import org.openapitools.client.models.UpdateCardEntity
import java.util.*

class UpdateCardViewModel(
    id: UUID,
    private val cardRepository: CardRepository = CardRepository(),
    private val categoryRepository: CategoryRepository = CategoryRepository()
) : CardViewModel(
    id = id,
    cardRepository = cardRepository,
    categoryRepository = categoryRepository
) {

    init {
        cardRepository.getCard(id = id,
            onSuccess = {
                errors.value = emptyList()
                cardId.value = it.id
                cardAnswer.value = it.answer
                cardQuestion.value = it.question
                cardTag.value = it.tag

                if (cardCategories.value.isEmpty()) {
                    cardCategories.value = it.categories.map { cat ->
                        CategorySelectItem(label = cat.label, id = cat.id, isChecked = true)
                    }
                } else {
                    cardCategories.value = cardCategories.value.map { category ->
                        if (it.categories.firstOrNull { cat -> category.id == cat.id } != null) {
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

