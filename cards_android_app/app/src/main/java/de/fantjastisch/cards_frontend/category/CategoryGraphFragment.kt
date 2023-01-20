@file:Suppress("IMPLICIT_CAST_TO_ANY")

package de.fantjastisch.cards_frontend.category

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(viewModel.categories.value) { category ->
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                var expanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    modifier = Modifier.padding(6.dp),
                    expanded = expanded,
                    onExpandedChange = {expanded = !expanded}
                ) {
                    TextField(
                        readOnly = true,
                        value = category.label,
                        onValueChange = {},
                        leadingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)},
                        trailingIcon = { CategoryContextMenu(navigator = navigator, id = category.id, label = category.label)},
                        colors = ExposedDropdownMenuDefaults.textFieldColors(containerColor = Color(android.graphics.Color.WHITE)),
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        if (category.subCategories.isEmpty()) {
                            DropdownMenuItem(
                                text = { Text("keine Unterkategorien") },
                                onClick = {
                                    expanded = false
                                }
                            )
                        } else {

                            category.subCategories.forEach {
                                val nameOfSubcategory = mutableStateOf("")
                                viewModel.categoryRepository.getCategory(id = it,
                                    onSuccess = {
                                        nameOfSubcategory.value = it.label
                                    },
                                    onFailure = {})
                                DropdownMenuItem(
                                    text = {
                                        Text(nameOfSubcategory.value) },
                                    onClick = {
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                    }
                }

            }
                }



