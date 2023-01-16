package de.fantjastisch.cards_frontend.category

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.fantjastisch.cards_frontend.category.CategoryGraphViewModel

@Composable
fun CategoryGraphFragment(
    modifier: Modifier = Modifier
) {
    val viewModel = viewModel { CategoryGraphViewModel() }
    LaunchedEffect(key1 = Unit, block = { viewModel.onPageLoaded() })

    // Ein RecyclerView -> Eine lange liste von Eintraegen
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(viewModel.categories.value) { category ->
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .weight(1f),
                    text = category.label
                )
                Text(
                    modifier = Modifier
                        .weight(1f),
//                    TODO backend: evtl eher die label rausgeben anstatt der ids? oder beides?
//                    TODO items hübsch machen.
                    text = category.subCategories.toString()
                )
            }
        }
    }
}


