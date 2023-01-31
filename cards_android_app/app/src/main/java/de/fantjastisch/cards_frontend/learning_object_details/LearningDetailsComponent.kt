package de.fantjastisch.cards_frontend.learning_object_details;

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.category.LearningModeSortDialog
import de.fantjastisch.cards_frontend.learning_box.LearningBoxWitNrOfCards
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearningDetailsComponent(
    learningBox: LearningBoxWitNrOfCards,
    learningObjectId: UUID
) {
    val navigator = LocalNavigator.current!!

    val dialogOpen = remember { mutableStateOf(false) }
    Box(
        modifier =
        if (learningBox.nrOfCards != 0) {
            Modifier
                .clickable(
                    onClick = { dialogOpen.value = true })
        } else {
            Modifier
        }
    ) {
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
                    if (learningBox.nrOfCards != 0) {
                        LearningDetailsContextMenu(
                            learningBoxId = learningBox.id,
                            learningObjectId = learningObjectId,
                        )
                    }
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
                        text = String.format(
                            "%s: %d",
                            stringResource(R.string.number_of_cards_text),
                            learningBox.nrOfCards
                        )
                    )
                    Spacer(
                        Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    )
                    Text(
                        modifier = Modifier,
                        text = String.format("Box-Nr. %d", (learningBox.boxNumber + 1))
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.inventory_2),
                        contentDescription = "Box",
                    )
                }
            }
        }
    }
    LearningModeSortDialog(
        learningBox = learningBox,
        learningObjectId = learningObjectId,
        isOpen = dialogOpen.value,
        setIsOpen = { dialogOpen.value = it },
        navigator = navigator
    )
}

