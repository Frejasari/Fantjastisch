package de.fantjastisch.cards_frontend.card

import androidx.compose.runtime.mutableStateOf
import de.fantjastisch.cards_frontend.category.CategoryRepository
import org.openapitools.client.models.CreateCardEntity

class CreateCardViewModel(
    private val cardRepository: CardRepository = CardRepository(),
    categoryRepository: CategoryRepository = CategoryRepository()
) : CardViewModel(
    cardRepository = cardRepository,
    categoryRepository = categoryRepository
) {

    // states, die vom View gelesen werden können -> automatisches Update vom View.
    val card = mutableStateOf(listOf<CardSelectItem>())


    override fun save() {
        error.value = null
        errors.value = emptyList()

        cardRepository.createCard(
            card = CreateCardEntity(
                question = cardQuestion.value,
                answer = cardAnswer.value,
                tag = cardTag.value,
                categories = cardCategories.value.filter { it.isChecked }
                    .map { it.id }
            ),
            onSuccess = {
                isFinished.value = true
            },
            onFailure = {
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