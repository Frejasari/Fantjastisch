package de.fantjastisch.cards_frontend.learning_object_details.cards_view.edit_cards_in_box

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.navigator.Navigator
import de.fantjastisch.cards_frontend.learning_object_details.cards_view.CardsInBoxContextMenu
import java.util.*


data class EditCardsInBoxFragment(
    val learningBoxId: UUID,
    val navigator: Navigator,
    val learningObjectId: UUID
) :
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
                title = { Text(text = "Karten in dieser Box") },
                actions = {
                    CardsInBoxContextMenu(
                        learningBoxId = learningBoxId,
                        navigator = navigator,
                        learningObjectId = learningObjectId
                    )
                })
        })
        {
            EditCardsInBoxView(
                modifier = Modifier.padding(it),
                viewModel = viewModel {
                    EditCardsInBoxViewModel(
                        learningBoxId = learningBoxId,
                        learningObjectId = learningObjectId
                    )
                },
            )
        }
    }
}

