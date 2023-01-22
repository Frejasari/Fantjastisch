package de.fantjastisch.cards_frontend.learning_mode

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.androidx.AndroidScreen
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.card.CardContentView

import de.fantjastisch.cards_frontend.card.LearningModeView

import java.util.*

data class LearningModeFragment(val learningBoxId: UUID, val learningObjectId: UUID) :
    AndroidScreen() {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        Scaffold(topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                title = { Text(stringResource(R.string.learning_mode_title)) },
            )
        })

        {
            LearningModeView(
                modifier = Modifier.padding(it),
                viewModel = viewModel {
                    LearningModeViewModel(
                        learningBoxId = learningBoxId,
                        learningObjectId = learningObjectId
                    )
                }
            )
        }
    }
}