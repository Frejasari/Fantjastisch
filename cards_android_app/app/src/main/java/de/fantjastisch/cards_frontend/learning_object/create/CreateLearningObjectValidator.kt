package de.fantjastisch.cards_frontend.learning_object.create

import de.fantjastisch.cards_frontend.components.SingleSelectItem
import org.openapitools.client.models.ErrorEntryEntity

/**
 * Validiert, die Eingabewerte, wenn ein neues Lernobjekt erstellt wird.
 *
 * @author Semjon Nirmann
 */
class CreateLearningObjectValidator {

    /**
     * Prüft, ob die Bezeichnung und das Lernsystem beim Erstellen eines Lernobjekts eingegeben wurden.
     *
     * @param selectedSystem Das ausgewählte Lernsystem.
     * @param learningObjectLabel Die eingegebene Bezeichnung des zu erstellenden Lernobjekts.
     * @return  MutableList<ErrorEntryEntity> Wenn beide Werte richtig eingegeben wurden, dann eine leere Liste, sonst
     *  eine Liste von [ErrorEntryEntity]
     */
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