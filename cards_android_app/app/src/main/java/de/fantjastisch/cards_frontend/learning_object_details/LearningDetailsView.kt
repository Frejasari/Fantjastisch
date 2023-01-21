package de.fantjastisch.cards_frontend.card

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.Navigator
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.learning_object_details.LearningDetailsContextMenu
import de.fantjastisch.cards_frontend.learning_object_details.LearningDetailsViewModel


@Composable
fun LearningDetailsView(
    modifier: Modifier = Modifier,
    viewModel: LearningDetailsViewModel,
    navigator: Navigator
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(viewModel.learningBoxes.value) { learningBox ->
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
                                .weight(weight = 1f, fill = false)
                                .padding(vertical = 8.dp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            text = learningBox.label,
                            fontSize = 18.sp
                        )
                        LearningDetailsContextMenu(
                            learningBoxId = learningBox.id,
                            navigator = navigator,
                            learningObjectId = viewModel.learningObjectId
                        )
                    }
                    Divider(
                        modifier = Modifier
                            .padding(vertical = 3.dp)
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier,
                            text = "Anzahl Karten: " + viewModel.getNumOfCardsFromLearningBox(
                                learningBox.id
                            )
                        )
                        Spacer(
                            Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        )
                        Text(
                            modifier = Modifier,
                            text = "Box-Nr. " + (learningBox.boxNumber + 1).toString()
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.inventory_2),
                            contentDescription = "Box",
                            // decorative element
                        )
                    }
                }
            }
        }
    }
}


