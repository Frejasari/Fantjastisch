@file:Suppress("IMPLICIT_CAST_TO_ANY")

package de.fantjastisch.cards_frontend.category

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.openapitools.client.models.CategoryEntity
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun CategoryGraphFragment(
    modifier: Modifier = Modifier
) {
    val navigator = LocalNavigator.currentOrThrow.parent!!
    val viewModel = viewModel { CategoryGraphViewModel() }



    LaunchedEffect(key1 = Unit, block = { viewModel.onPageLoaded() })

    // Ein RecyclerView -> Eine lange liste von Eintraegen
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(viewModel.categories.value) { category ->
            Surface(
                modifier = Modifier,
                shadowElevation = 6.dp,
            ) { Row(
                modifier = Modifier
                    .fillMaxWidth().padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                var expanded by remember { mutableStateOf(false) }
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
                    colors = CardDefaults.cardColors(containerColor =  Color.Transparent),
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
                            CategoryContextMenu(navigator = navigator, id = category.id, label = category.label)
                        }

                        if (expanded) {

                            if (category.subCategories.isEmpty()) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .weight(6f).padding(start = 16.dp),
                                        text = "keine Unterkategorien"
                                    )
                                }
                            } else {
                                category.subCategories.forEach {
                                    val nameOfSubcategory = remember { mutableStateOf("") }
                                    viewModel.categoryRepository.getCategory(id = it,
                                        onSuccess = {
                                            nameOfSubcategory.value = it.label
                                        },
                                        onFailure = {})
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            modifier = Modifier
                                                .weight(6f).padding(start = 16.dp),
                                            text = nameOfSubcategory.value)
                                        CategoryContextMenu(navigator = navigator, id = it, label = nameOfSubcategory.value)
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }
        }
    }
}




