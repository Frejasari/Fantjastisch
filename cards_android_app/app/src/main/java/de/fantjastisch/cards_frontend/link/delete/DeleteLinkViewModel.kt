/* package de.fantjastisch.cards_frontend.link.delete

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import de.fantjastisch.cards_frontend.card.CardRepository
import de.fantjastisch.cards_frontend.link.LinkRepository
import java.util.*

class DeleteLinkViewModel(
    private val linkRepository: LinkRepository = LinkRepository(),
    private val linkId: UUID
    // : ViewModel() = extends ViewModel
) : ViewModel() {

    val error = mutableStateOf<String?>(null)
    val isFinished = mutableStateOf(false)

    fun onDeleteClicked() {
        error.value = null
        linkRepository.deleteLink(
            id = linkId,
            onSuccess = {
                isFinished.value = true
            },
            onFailure = {
                // Fehler anzeigen:
                error.value = "Irgendwas ist schief gelaufen"

            }
        )
    }

} */