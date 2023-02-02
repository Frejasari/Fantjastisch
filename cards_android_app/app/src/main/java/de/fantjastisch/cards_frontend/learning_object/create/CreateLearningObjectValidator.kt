package de.fantjastisch.cards_frontend.learning_object.create

import de.fantjastisch.cards_frontend.components.SingleSelectItem
import org.openapitools.client.models.ErrorEntryEntity

class CreateLearningObjectValidator {

    fun validate(
        selectedSystem: SingleSelectItem?,
        learningObjectLabel: String
    ): MutableList<ErrorEntryEntity> {
        val errors = mutableListOf<ErrorEntryEntity>()
        val code = ErrorEntryEntity.Code.cONSTRAINTVIOLATION
        if (selectedSystem == null) {

            errors.add(
                ErrorEntryEntity(
                    code = code,
                    field = "learningsystem",
                    message = "Darf nicht null sein"
                )
            )
        }
        if (selectedSystem == null || learningObjectLabel.isEmpty()) {
            errors.add(
                ErrorEntryEntity(
                    code = code,
                    field = "label",
                    message = "Darf nicht null sein"
                )
            )
        }
        return errors
    }
}