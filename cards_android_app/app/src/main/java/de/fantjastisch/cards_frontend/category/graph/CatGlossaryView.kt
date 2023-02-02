package de.fantjastisch.cards_frontend.category.graph

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.category.CategoryContextMenu
import de.fantjastisch.cards_frontend.category.DeleteCategoryDialog
import de.fantjastisch.cards_frontend.infrastructure.FantMainNavigator
import org.openapitools.client.models.CategoryEntity


@Composable
@Preview
fun CategoryGraphView(modifier: Modifier = Modifier) {

    val viewModel = viewModel { CatGlossaryViewModel() }

    val deletionProgress = viewModel.currentDeleteDialog.value
    if (deletionProgress != null) {
        DeleteCategoryDialog(
            cat = deletionProgress.cat,
            isDeleteButtonEnabled = deletionProgress is CatGlossaryViewModel.DeletionProgress.ConfirmWithUser,
            onDismissClicked = { viewModel.onDeleteCategoryAborted() },
            onDeleteClicked = {
                viewModel.onDeleteCategoryClicked()
            }
        )
    }

    // Lädt die Cards neu, wenn wir aus einem anderen Tab wieder hier rein kommen.
    LaunchedEffect(
    // wenn sich diese Variable ändert
    key1 = Unit,
    // dann wird dieses Lambda ausgeführt.
    block = {
        viewModel.onPageLoaded()
    })

    val listState = rememberLazyListState()


    // Ein RecyclerView -> Eine lange liste von Eintraegen
    LazyColumn(
    modifier = modifier
    .fillMaxWidth()
    .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(10.dp),
    contentPadding = PaddingValues(vertical = 16.dp),
    state = listState
    ) {
        itemsIndexed(viewModel.categories.value) { index, category ->
            CategoryView(category, viewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState")
@Composable
private fun CategoryView(
    category: CategoryEntity,
    viewModel: CatGlossaryViewModel,
) {
    var expanded by remember { mutableStateOf(false) }
    val rotate by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f
    )

    FantMainNavigator.current
    val context = LocalContext.current



    Surface(
        modifier = Modifier,
        shadowElevation = 6.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

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
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.weight(5f),
                            text = category.label,
                            fontWeight = FontWeight.Bold,
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
                        CategoryContextMenu(
                            id = category.id,
                            onDeleteClicked = {
                                if(category.subCategories.isNotEmpty()) {
                                    Toast.makeText(context, R.string.categories_delete_error, Toast.LENGTH_SHORT).show()
                                } else {
                                    viewModel.onTryDeleteCategory(category)
                                }
                                 }
                        )
                    }

                    if (expanded) {

                        if (category.subCategories.isEmpty()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(6f)
                                        .padding(start = 16.dp),
                                    text = stringResource(R.string.no_subcategories)
                                )
                            }
                        } else {
                            category.subCategories.forEach {
                                val nameOfSubcategory = remember { mutableStateOf("") }
                                nameOfSubcategory.value = viewModel.categories.value.filter {category -> category.id == it}.map{category -> category.label}.first()
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        modifier = Modifier
                                            .weight(6f)
                                            .padding(start = 16.dp),
                                        text = nameOfSubcategory.value
                                    )
                                    CategoryContextMenu(
                                        id = category.id,
                                        onDeleteClicked = { viewModel.onTryDeleteCategory(category) }
                                    )
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}