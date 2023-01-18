package de.fantjastisch.cards_frontend.category


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.navigator.Navigator
import de.fantjastisch.cards.R
import org.openapitools.client.models.CategoryEntity
import java.util.UUID



@Composable
fun SubCategoryMenu(navigator: Navigator, id : UUID, cat : CategoryEntity) {
        val isMenuOpen = remember { mutableStateOf(false) }
        val viewModel = viewModel { CategoryGraphViewModel() }

        IconButton(onClick = { isMenuOpen.value = !isMenuOpen.value }) {
            Icon(Icons.Outlined.ArrowDropDown, contentDescription = "context actions")

            DropdownMenu(
                expanded = isMenuOpen.value,
                onDismissRequest = { isMenuOpen.value = false }
            ) {
                cat.subCategories.forEach{
                DropdownMenuItem(
                    text = { Text(getString(viewModel, cat)) },
                    onClick = { isMenuOpen.value = false })
            } }
        }
}

fun getString(viewModel : CategoryGraphViewModel, cat : CategoryEntity) : String{
    val sub = arrayListOf<String>()
    val lst = cat.subCategories
    var i = 0
    lst.forEach {
        viewModel.onCategoryLoaded(it)
        if (viewModel.cat.isNotEmpty()) {
            val test = viewModel.cat
            val subCat = viewModel.cat.get(i)
            sub.add(subCat.label)
        }
        ++i
    }
    return sub.toString()
}