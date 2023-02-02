package de.fantjastisch.cards_frontend.learning_object.create

import de.fantjastisch.cards_frontend.components.SingleSelectItem
import de.fantjastisch.cards_frontend.util.mapError
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
                    message = mapError(code)
                )
            )
        }
        if (selectedSystem == null || learningObjectLabel.isEmpty()) {
            errors.add(ErrorEntryEntity(code = code, field = "label", message = mapError(code)))
        }
        return errors
    }
}