package de.fantjastisch.cards_frontend.card.update_and_create

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.androidx.AndroidScreen

import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.card.UpdateAndCreateCardView

class CreateCardFragment : AndroidScreen() {
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
                title = { Text(text = stringResource(id = R.string.create_card_headline)) },
            )
        })
        {
            UpdateAndCreateCardView(modifier = Modifier.padding(it),
                viewModel = viewModel { CreateCardViewModel() }
            )
        }
    }
}
