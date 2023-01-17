package de.fantjastisch.cards_frontend.glossary

//import de.fantjastisch.cards_frontend.category.CategoryGraphFragment
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.fantjastisch.cards_frontend.card.CardContextMenu


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun GlossaryView(
    modifier: Modifier = Modifier
) {

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
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp),

                    ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier
                                .weight(weight = 1f, fill = false),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            text = card.question
                        )
                        CardContextMenu(
                            cardId = card.id,
                            tag = card.tag
                        )
                    }

                    Divider(
                        modifier = Modifier
                            .padding(vertical = 6.dp)
                    )
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

