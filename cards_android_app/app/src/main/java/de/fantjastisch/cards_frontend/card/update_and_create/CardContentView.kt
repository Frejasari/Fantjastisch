package de.fantjastisch.cards_frontend.card

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuDefaults.textFieldColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.card.update_and_create.CardContentViewModel
import de.fantjastisch.cards_frontend.card.update_and_create.UpdateAndCreateCardViewModel
import de.fantjastisch.cards_frontend.category.CategoryContextMenu
import de.fantjastisch.cards_frontend.category.CategorySelect
import de.fantjastisch.cards_frontend.components.OutlinedTextFieldWithErrors
import org.openapitools.client.models.ErrorEntryEntity
import java.util.*


//TODO Fehler anzeigen.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardContentView(
    modifier: Modifier = Modifier,
    viewModel: CardContentViewModel
) {

    //var navigator = LocalNavigator.currentOrThrow.parent!!

    // Componente die ihre Kinder untereinander anzeigt.
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextField(
            value = viewModel.cardQuestion.value,
            onValueChange = { viewModel.cardQuestion.value = it },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            label = { Text(stringResource(id = R.string.create_card_question_text)) },
            maxLines = 5,
            colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent)
        )
        TextField(
            value = viewModel.cardAnswer.value,
            onValueChange = { viewModel.cardAnswer.value = it },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            label = { Text(stringResource(id = R.string.create_card_answer_text)) },
            maxLines = 5,
            colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent)
        )
        TextField(
            value = viewModel.cardTag.value,
            onValueChange = { viewModel.cardTag.value = it },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            label = { Text(stringResource(id = R.string.create_card_tag_text)) },
            maxLines = 5,
            colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent)
        )


            var expanded by remember { mutableStateOf(true) }
            val rotate by animateFloatAsState(
                targetValue = if (expanded) 180f else 0f
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = LinearOutSlowInEasing
                        )
                    ),
                shape = RoundedCornerShape(4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                onClick = { expanded = !expanded },
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth().padding(vertical = 8.dp, horizontal = 16.dp),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.weight(5f),
                            text = stringResource(id = R.string.create_card_categories_text),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        IconButton(
                            modifier = Modifier
                                .weight(1f)
                                .rotate(rotate),
                            onClick = {
                                expanded = !expanded
                            }) {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "drop-down arrow"
                            )
                        }
                    }

                    if (expanded) {
                        viewModel.cardCategories.value.forEach {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(6f).padding(start = 16.dp),
                                text = it
                            )
                        }
                    }
                }
            }

            val navigator = LocalNavigator.currentOrThrow
            // einmaliger Effekt
            LaunchedEffect(
                // wenn sich diese Variable ändert
                key1 = viewModel.isFinished.value,
                // dann wird dieses Lambda ausgeführt.
                block = {
                    if (viewModel.isFinished.value) {
                        navigator.pop()
                    }
                })
        }
    }
}



