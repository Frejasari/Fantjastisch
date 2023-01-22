 package de.fantjastisch.cards_frontend.link.update_and_create

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.card.CardRepository
import de.fantjastisch.cards_frontend.card.CardSelectItem
import de.fantjastisch.cards_frontend.category.CategorySelectItem
import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import de.fantjastisch.cards_frontend.link.LinkRepository
import de.fantjastisch.cards_frontend.link.LinkSelectItem
import de.fantjastisch.cards_frontend.link.create.CreateLinkModel
import kotlinx.coroutines.launch
import org.openapitools.client.models.CreateCardEntity
import org.openapitools.client.models.CreateCategoryEntity
import org.openapitools.client.models.CreateLinkEntity
import org.openapitools.client.models.ErrorEntryEntity
import java.util.*


 class CreateLinkViewModel(
     private val sourceId: UUID,
     private val createLinkModel: CreateLinkModel = CreateLinkModel(sourceId = sourceId)
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
                 error.value = "Check network connection"
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
                 is RepoResult.ServerError -> error.value = "Irgendwas ist schief gelaufen"
             }
         }
     }
 }