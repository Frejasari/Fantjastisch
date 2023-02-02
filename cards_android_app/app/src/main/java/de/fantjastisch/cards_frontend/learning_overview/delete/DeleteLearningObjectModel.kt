package de.fantjastisch.cards_frontend.learning_overview.delete

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.infrastructure.RepoResult
import de.fantjastisch.cards_frontend.infrastructure.fold
import de.fantjastisch.cards_frontend.learning_object.LearningObjectRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class DeleteLearningObjectModel(
    private val learningObjectRepository: LearningObjectRepository = LearningObjectRepository(),
) : ViewModel() {

    val error = mutableStateOf<String?>(null)
    val isFinished = mutableStateOf(false)

    suspend fun deleteLearningObject(learningObjectId: UUID) : RepoResult<Unit> = learningObjectRepository.delete(id = learningObjectId)
}