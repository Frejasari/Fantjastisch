package de.fantjastisch.cards_frontend.card

//import de.fantjastisch.cards_frontend.category.CategoryGraphFragment
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlossaryView(
    modifier: Modifier = Modifier
) {
    val navigator = LocalNavigator.currentOrThrow
    val viewModel = viewModel { GlossaryViewModel() }
    LaunchedEffect(
        // wenn sich diese Variable ändert
        key1 = Unit,
        // dann wird dieses Lambda ausgeführt.
        block = {
            viewModel.onPageLoaded()
        })
    // Ein RecyclerView -> Eine lange liste von Eintraegen
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(viewModel.cards.value) { card ->
            Surface(
                modifier = Modifier,
                shadowElevation = 6.dp,
                onClick = {
                    navigator.push(UpdateCardFragment(card.id))}
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp),

                ) {
                    Text(
                        modifier = Modifier,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        text = card.question
                    )
                    Divider(modifier = Modifier.padding(vertical = 6.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier,
                            text = card.tag
                        )
                        card.categories.forEach {
                            SuggestionChip(
                                modifier = Modifier,
                                onClick = { },
                                label = {
                                    Text(
                                        modifier = Modifier,
                                        text = it.label
                                    )
                                })
                        }
                    }
                }
            }
        }
    }
}

