package de.fantjastisch.cards_frontend.category.update_and_create

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.androidx.AndroidScreen
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.category.UpdateOrCreateCategoryView

class CreateCategoryFragment : AndroidScreen() {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                    title = { Text(text = stringResource(id = R.string.create_category_headline)) },
                )
            }) {
            UpdateOrCreateCategoryView(modifier = Modifier.padding(it),
                viewModel = CreateCategoryViewModel()
            )

        }
    }
}