package de.fantjastisch.cards_frontend.learning_object_details.cards_view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.card.CommonCardComponent
import de.fantjastisch.cards_frontend.card.UpdateAndCreateCardView
import de.fantjastisch.cards_frontend.card.update_and_create.UpdateCardViewModel
import de.fantjastisch.cards_frontend.infrastructure.GlossaryTab
import de.fantjastisch.cards_frontend.infrastructure.TobBarCreateMenu
import java.util.*


data class CardsInBoxFragment(val learningBoxId: UUID, val navigator: Navigator) : AndroidScreen() {

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
                actions = {
                    CardsInBoxContextMenu(learningBoxId = learningBoxId, navigator = navigator)
                })})
        {
            CardsInBoxView(
                modifier = Modifier.padding(it),
                viewModel= viewModel {CardsInBoxViewModel(learningBoxId)}
            )
        }
    }
}

