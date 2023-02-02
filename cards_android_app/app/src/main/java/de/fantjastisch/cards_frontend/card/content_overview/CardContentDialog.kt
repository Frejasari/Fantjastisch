package de.fantjastisch.cards_frontend.card.content_overview


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.glossary.LinkWithoutDeleteComponent
import java.util.*



@Composable
fun CardContentDialog(
    id: UUID,
    setIsOpen: (isOpen: Boolean) -> Unit,
    isOpen: Boolean
) {
    val viewModel = viewModel(key = id.toString()) { CardContentViewModel(id = id) }

    if (isOpen) {
        AlertDialog(
            onDismissRequest = { setIsOpen(false) },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
            ),
            text = {
                Column(
                    modifier = Modifier.padding(6.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(weight = 1f)
                        ) {
                            Text(
                                text = stringResource(R.string.question_label),
                                fontWeight = FontWeight.Light,
                            )
                            Text(
                                text = viewModel.cardQuestion.value,
                            )
                        }
                    }
                    Divider(
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.answer_label),
                            fontWeight = FontWeight.Light,
                            fontSize = 12.sp
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = viewModel.cardAnswer.value
                        )
                    }
                    Divider(
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.tag_label),
                            fontWeight = FontWeight.Light,
                            fontSize = 12.sp
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier,
                            text = viewModel.cardTag.value,
                        )
                    }
                    Divider(
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.categories_label),
                            fontWeight = FontWeight.Light,
                            fontSize = 12.sp
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.weight(4.25f),
                            text = viewModel.cardCategories.value.joinToString(separator = ", ")
                            { category -> category.label },
                        )
                    }
                    Divider(
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                    ) {
                        Text(
                            text = stringResource(R.string.links_label),
                            fontWeight = FontWeight.Light,
                            fontSize = 12.sp
                        )
                    }
                    LazyRow(
                        modifier= Modifier.fillMaxWidth()
                    ) {
                        item {
                            viewModel.cardLinks.value.forEach {
                                LinkWithoutDeleteComponent(link = it)
                            }
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {}
        )
    }
}