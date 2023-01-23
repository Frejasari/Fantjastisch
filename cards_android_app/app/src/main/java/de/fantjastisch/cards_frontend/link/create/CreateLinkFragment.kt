 package de.fantjastisch.cards_frontend.link.create

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.androidx.AndroidScreen

import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.card.update.TextFieldState
import de.fantjastisch.cards_frontend.infrastructure.CloseScreenOnSignalEffect
import de.fantjastisch.cards_frontend.link.LinkEdit
import java.util.*

class CreateLinkFragment(val id: UUID) : AndroidScreen() {
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
                title = { Text(text = stringResource(id = R.string.create_link_headline)) },
            )
        })
        {
            CreateLinkView(
                modifier = Modifier.padding(it),
                sourceId = id
            )
        }
    }

    private @Composable
    fun CreateLinkView(modifier: Modifier = Modifier,
                       sourceId: UUID
    ) {

    val viewModel = viewModel { CreateLinkViewModel(sourceId = sourceId) }

    LinkEdit(
        modifier = modifier,
        name = TextFieldState(
            value = viewModel.linkName.value,
            errors = viewModel.errors.value,
            onValueChange = viewModel::setLinkName,
        ),
        cards = viewModel.linkCards.value,
        onCardSelected = viewModel::onCardSelected,
        onUpdateLinkClicked = viewModel::onCreateLinkClicked,
    )
        CloseScreenOnSignalEffect(shouldClose = viewModel.isFinished.value)
    }

}
