package de.fantjastisch.cards_frontend.learning_object_details.cards_view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.androidx.AndroidScreen
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.card.CommonCardComponent
import de.fantjastisch.cards_frontend.card.UpdateAndCreateCardView
import de.fantjastisch.cards_frontend.card.update_and_create.UpdateCardViewModel
import java.util.*


data class CardsInBoxFragment(val learningBoxId: UUID) : AndroidScreen() {
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
                title = { Text(text = "Karten in dieser Box") },
            )
        })
        {
            CardsInBoxView(
                modifier = Modifier.padding(it),
                viewModel= viewModel {CardsInBoxViewModel(learningBoxId)}
            )
        }
    }
}

