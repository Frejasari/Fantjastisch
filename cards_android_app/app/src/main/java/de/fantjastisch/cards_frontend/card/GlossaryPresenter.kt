package de.fantjastisch.cards_frontend.card

import de.fantjastisch.cards_frontend.validation.ValidationException
import org.openapitools.client.models.CardEntity
import org.openapitools.client.models.CreateCardEntity
import java.util.*

class GlossaryPresenter(private val view: GlossaryFragment) {

    private val repo = CardRepository()

    fun onSaveClicked(answer: String, question: String, tag: String, id: UUID? = null) {
            validate(answer, question, id)
            repo.createCard(
                CreateCardEntity(
                    question = question,
                    answer = answer,
                    categories = listOf(),
                    tag = tag
                ),
                {
                    view.showSucces(CardEntity(
                    question = question,
                    answer = answer,
                    categories = listOf(),
                    tag = tag,
                    id = UUID.fromString(it)
                ))},
                { view.showError("Something went wrong") }
            )

    }

    private fun validate(answer: String?, question: String?, id: UUID?) {
        val errors = mutableListOf<String>()
        if (question.isNullOrBlank()) {
            errors.add("question is empty");
        }
        if (answer.isNullOrBlank()) {
            errors.add("answer is empty");
        }
        if (errors.isNotEmpty()) {
            throw ValidationException(errors)
        }
    }
}