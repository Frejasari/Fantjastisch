package de.fantjastisch.cards_frontend.learning_object_details

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.navigator.Navigator
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.card.LearningDetailsView
import java.util.*

data class LearningDetailsFragment(val id: UUID, val navigator: Navigator) : AndroidScreen() {
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
                title = { Text(stringResource(R.string.learning_object_detail)) },
            )
        })

        {
            LearningDetailsView(
                viewModel = viewModel { LearningDetailsViewModel(id) },
                modifier = Modifier.padding(it),
                navigator = navigator
            )
        }
    }

    // companion object : SingletonHolder<UpdateCardFragment, UUID>(::UpdateCardFragment)
}