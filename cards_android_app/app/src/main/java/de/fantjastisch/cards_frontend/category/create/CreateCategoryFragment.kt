package de.fantjastisch.cards_frontend.category.create

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.androidx.AndroidScreen
import de.fantjastisch.cards.R

/**
 * Zeigt den Screen um eine Kategorie zu erstellen
 *
 * @author Tamari Bayer, Freja Sender
 */
class CreateCategoryFragment : AndroidScreen() {
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
                title = { Text(text = stringResource(id = R.string.create_category_headline)) },
            )
        })
        {
            CreateCategoryView(
                modifier = Modifier.padding(it)
            )
        }
    }
}
