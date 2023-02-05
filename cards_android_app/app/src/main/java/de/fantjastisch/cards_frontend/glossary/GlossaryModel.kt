package de.fantjastisch.cards_frontend.glossary

import de.fantjastisch.cards_frontend.card.CardRepository
import de.fantjastisch.cards_frontend.learning_box.card_to_learning_box.CardToLearningBoxRepository
import de.fantjastisch.cards_frontend.util.RepoResult
import de.fantjastisch.cards_frontend.util.RepoResult.*
import de.fantjastisch.cards_frontend.util.RepoResult.UnexpectedErrorType.NETWORK_ERROR
import de.fantjastisch.cards_frontend.util.RepoResult.UnexpectedErrorType.UNEXPECTED_ERROR
import de.fantjastisch.cards_frontend.util.isNetworkError
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.util.*

/**
 * Kapselt die Logik für das [GlossaryViewModel].
 * Fungiert als Vermittler zwischen Repository und ViewModel.
 *
 * @property cardToLearningBoxRepository Repository Karten in Lernboxen
 * @property cardRepository Repository Karten
 *
 * @author Freja Sender, Jessica Repty, Tamari Bayer
 */
class GlossaryModel(
    private val cardToLearningBoxRepository: CardToLearningBoxRepository = CardToLearningBoxRepository(),
    private val cardRepository: CardRepository = CardRepository(),
) {

    /**
     * Sendet eine Anfrage für das Holen aller Karten an das Backend-Repository für Karten.
     *
     * @param categoryIds Die Liste der Kategorien-UUIDs, wonach alle Karten gefiltert werden sollen.
     * @param search Ein (Teil-)String, wonach entweder in der Frage oder Antwort gesucht wird.
     * @param tag Das Schlagwort, wonach gesucht wird.
     * @param sort Alphabetische Sortierung von Tags.
     */
    suspend fun getCards(
        categoryIds: List<UUID>,
        search: String,
        tag: String,
        sort: Boolean
    ) = cardRepository.getPage(
        categoryIds = categoryIds.ifEmpty { null },
        search = search.ifEmpty { null },
        tag = tag.ifEmpty { null },
        sort = sort
    )


    /**
     * Sendet eine Anfrage an das Backend-Repository für Karten.
     * Im Erfolgsfall wird eine Karte gelöscht.
     *
     * @param cardId UUID von der Karte, die gelöscht werden soll.
     * @return RepoResult<Unit> (OnSuccess, OnUnexpectedError, ...)
     */
    suspend fun deleteCard(cardId: UUID): RepoResult<Unit> = coroutineScope {
        val (apiResult, dbResult) = awaitAll(
            async { cardRepository.deleteCard(cardId = cardId) },
            async {
                cardToLearningBoxRepository.deleteCard(
                    cardId = cardId
                )
            },
        )
        when {
            apiResult is Success && dbResult is Success -> {
                Success(Unit)
            }
            apiResult is Error -> Error(apiResult.errors)
            apiResult.isNetworkError() || dbResult.isNetworkError() -> ServerError(NETWORK_ERROR)
            else -> ServerError(UNEXPECTED_ERROR)
        }
    }


}