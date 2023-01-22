 package de.fantjastisch.cards_frontend.link.update

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.androidx.AndroidScreen
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.card.update.TextFieldState
import de.fantjastisch.cards_frontend.card.update.UpdateCardViewModel
import de.fantjastisch.cards_frontend.card.update_and_create.CardEdit
import de.fantjastisch.cards_frontend.infrastructure.CloseScreenOnSignalEffect
import de.fantjastisch.cards_frontend.link.LinkEdit
import de.fantjastisch.cards_frontend.link.update_and_create.UpdateLinkViewModel
import java.util.*

data class UpdateLinkFragment(val linkId: UUID, val sourceId: UUID) : AndroidScreen() {
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
                title = { Text(text = stringResource(id = R.string.update_link_headline)) },
            )
        })
        {
            UpdateLinkView(
                modifier = Modifier.padding(it),
                linkId = linkId,
                sourceId = sourceId
            )
        }
    }

    private @Composable
    fun UpdateLinkView(modifier: Modifier = Modifier, linkId: UUID, sourceId: UUID) {
        val viewModel = viewModel { UpdateLinkViewModel(linkId = linkId, sourceId = sourceId) }
        // Componente die ihre Kinder untereinander anzeigt.
        LinkEdit(
            modifier = modifier,
            name = TextFieldState(
                value = viewModel.linkName.value,
                errors = viewModel.errors.value,
                onValueChange = viewModel::setLinkName,
            ),
            cards = viewModel.linkCards.value,
            onCardSelected = viewModel::onCardSelected,
            onUpdateCardClicked = viewModel::onUpdateLinkClicked,
        )

        CloseScreenOnSignalEffect(shouldClose = viewModel.isFinished.value)

    }
}
