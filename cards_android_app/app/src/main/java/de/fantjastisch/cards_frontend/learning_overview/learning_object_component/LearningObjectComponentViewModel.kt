package de.fantjastisch.cards_frontend.learning_overview.learning_object_component

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import de.fantjastisch.cards_frontend.card.update_and_create.ErrorHandlingViewModel
import de.fantjastisch.cards_frontend.infrastructure.fold
import kotlinx.coroutines.launch
import java.util.*

class LearningObjectComponentViewModel(
    private val learningSystemId: UUID,
    private val learningObjectId: UUID,
    private val model: LearningObjectComponentModel = LearningObjectComponentModel()
) : ErrorHandlingViewModel() {

    val progress = mutableStateOf(0)
    val learningSystemLabel = mutableStateOf("")
    val isLoading = mutableStateOf(true)

    init {
        onPageLoaded()
    }

    private fun onPageLoaded() {
        viewModelScope.launch {
            model.initializePage(
                learningSystemId = learningSystemId,
                learningObjectId = learningObjectId
            ).fold(
                onSuccess = {
                    learningSystemLabel.value = it.learningSystemLabel
                    progress.value = it.progress
                },
                onValidationError = ::setValidationErrors,
                onUnexpectedError = ::setUnexpectedErrors,
            )
            isLoading.value = false
        }

    }

    fun getColor(): Color {
        val color: Color = when {
            progress.value < 33 -> Color(0xFFC53030)
            progress.value < 66 -> Color(0xFFFF8707)
            else -> Color(0xFF2B990D)
        }
        return color
    }
}