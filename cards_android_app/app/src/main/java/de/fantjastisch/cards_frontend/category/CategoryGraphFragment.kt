@file:Suppress("IMPLICIT_CAST_TO_ANY")

package de.fantjastisch.cards_frontend.category

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.openapitools.client.models.CategoryEntity
import java.util.*
import java.util.Locale.Category

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
                Text(
                    modifier = Modifier
                        .weight(1f),
                    text = category.label
                )
                SubCategoryMenu(navigator = navigator, id = category.id, cat = category)
                CategoryContextMenu(navigator = navigator, id = category.id)
              /*  Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    category.subCategories.forEach {
                        Column {
                            ListItem(
                                headlineText = { Text("One line list item with 24x24 icon") },
                                leadingContent = {}
                            )
                            Divider()
                        } */
                      /*  SuggestionChip(
                            modifier = Modifier,
                            onClick = { },
                            label = {
                                Text(
                                    modifier = Modifier,
                                    text = it.toString().take(5)
                                )
                            })*/
                    }
                    //  getNames(cat = category).toString()
                    //category.subCategories.toString()
                    /*    val sub = arrayListOf<String>()
                        val lst = category.subCategories
                        lst.forEach {
                            viewModel.onPageLoaded(it)
                            if (viewModel.cat.isNotEmpty()) {
                                val test = viewModel.cat
                                val subCat = viewModel.cat.get(viewModel.cat.size-1)
                                sub.add(subCat.label)
                            }
                            }
                        sub.toString()   */
                }
            }
                }





@Composable
fun getNames(cat:CategoryEntity) : ArrayList<String>{
    val viewModel = viewModel { CategoryGraphViewModel() }
    val result = ArrayList<String>()
    cat.subCategories.forEach{
        viewModel.categoryRepository.getCategory(id = it,
            onSuccess = {
                result.add(it.label)
            },
            onFailure = {})
    }
    return result
}



