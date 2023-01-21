package de.fantjastisch.cards_frontend.learning_object_details.cards_view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.androidx.AndroidScreen
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

