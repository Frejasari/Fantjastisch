package de.fantjastisch.cards_frontend.card

import de.fantjastisch.cards_frontend.config.client
import de.fantjastisch.cards_frontend.util.RepoResult
import de.fantjastisch.cards_frontend.util.toRepoResult
import org.openapitools.client.apis.CardApi
import org.openapitools.client.models.CardEntity
import org.openapitools.client.models.CreateCardEntity
import org.openapitools.client.models.UpdateCardEntity
import retrofit2.awaitResponse
import java.util.*

/**
 * Repository kommuniziert mit dem CardBackend.
 *
 * @author Freja Sender, Tamari Bayer
 */
class CardRepository {

    private val service = client.createService(CardApi::class.java)

    /**
     * Sendet eine Datenbankanfrage an das Backend und kriegt im Erfolgsfall für die
     * übergebene Id, die passende Karte.
     *
     * @param id Id, der gesuchten Karte.
     * @return RepoResponse<CardEntity> OnSuccess: Karte als [CardEntity]-Entität.
     */
    suspend fun getCard(
        id: UUID,
    ): RepoResult<CardEntity> = service.getCard(id).awaitResponse().toRepoResult()

    /**
     * Sendet eine Anfrage an das Backend und kriegt im Erfolgsfall alle
     * vorhandenen Karten zurück. Dabei können Filterfunktionen angewendet werden.
     *
     * @param categoryIds Liste an Id´s von Kategorien, zum Filtern jener Kategorien.
     * @param search Filtern nach Begriff, welcher auf der Antwort / Frage der Karten gesucht wird.
     * @param tag Filtern nach exaktem Schlagwort, welches die Karten tragen sollen.
     * @param sort Wenn true, dann sind Karten alphabetisch sortiert.
     * @return RepoResult<List<CardEntity>> OnSuccess: Liste an eventuell gefilterten
     *         Karten als [CardEntity]-Entitäten.
     */
    suspend fun getPage(
        categoryIds: List<UUID>?,
        search: String?,
        tag: String?,
        sort: Boolean?
    ): RepoResult<List<CardEntity>> = service.getCardPage(
        categoryFilter = categoryIds,
        search = search,
        tag = tag,
        sort = sort
    ).awaitResponse().toRepoResult()

    /**
     * Sendet eine Anfrage an das Backend, um eine Karte in die Datenbank zu speichern.
     *
     * @param card Karte, welche erzeugt werden soll.
     * @return RepoResponse<Unit> (OnSuccess, OnUnexpectedError, ...)
     */
    suspend fun createCard(
        card: CreateCardEntity,
    ): RepoResult<String> = service.createCard(card).awaitResponse().toRepoResult()

    /**
     * Sendet eine Anfrage an das Backend, um eine bestehende Karte in der Datenbank zu überschreiben.
     *
     * @param card Karte, welche überschrieben werden soll.
     * @return RepoResponse<Unit> (OnSuccess, OnUnexpectedError, ...)
     */
    suspend fun updateCard(
        card: UpdateCardEntity,
    ): RepoResult<Unit> = service.updateCard(card).awaitResponse().toRepoResult()

    /**
     * Sendet eine Anfrage an das Backend, um eine bestehende Karte aus der Datenbank zu löschen.
     *
     * @param cardId Id der Karte, welche gelöscht werden soll.
     * @return RepoResponse<Unit> (OnSuccess, OnUnexpectedError, ...)
     */
    suspend fun deleteCard(
        cardId: UUID,
    ): RepoResult<Unit> = service.deleteCard(cardId).awaitResponse().toRepoResult()
}
