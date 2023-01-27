 package de.fantjastisch.cards_frontend.link.create

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.card.CardSelectItem
import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import de.fantjastisch.cards_frontend.link.LinkSelectItem
import kotlinx.coroutines.launch
import org.openapitools.client.models.ErrorEntryEntity
import java.util.*


 class CreateLinkViewModel(
     private val sourceId: UUID,
     private val createLinkModel: CreateLinkModel = CreateLinkModel()
 ) :ViewModel() {


     val link = mutableStateOf(listOf<LinkSelectItem>())

     val linkId = mutableStateOf<UUID?>(null)
     val errors = mutableStateOf<List<ErrorEntryEntity>>(emptyList())
     val error = mutableStateOf<String?>(null)
     val isFinished = mutableStateOf(false)

     val linkName = mutableStateOf("")
     val linkSource = mutableStateOf("")
     val linkTarget = mutableStateOf<UUID?>(null)
     val linkCards = mutableStateOf(listOf<CardSelectItem>())

     init {
         viewModelScope.launch {
             val result = createLinkModel.getCards()

             if (result == null) {
                 error.value = "Ein Netzwerkfehler ist aufgetreten."
             } else {
                 errors.value = emptyList()
                 linkCards.value = result
             }
         }
     }

     fun setLinkName(value: String) {
         linkName.value = value
     }

     fun onCardSelected(id: UUID) {
         linkCards.value = linkCards.value.map {
             if (it.card.id == id) {
                 it.copy(isChecked = !it.isChecked)
             } else {
                 it
             }
         }
     }

     fun onCreateLinkClicked() {
         error.value = null
         errors.value = emptyList()

         viewModelScope.launch {
             val result = createLinkModel.createLink(
                 name = linkName.value,
                 sourceId = sourceId,
                 targetId = linkCards.value[0].card.id,
             )

             when (result) {
                 is RepoResult.Success -> isFinished.value = true
                 is RepoResult.Error -> errors.value = result.errors
                 is RepoResult.ServerError -> error.value = "Ein Netzwerkfehler ist aufgetreten."
             }
         }
     }
 }