package de.fantjastisch.cards_frontend.util

import de.fantjastisch.cards_frontend.category.CategorySelectItem
import org.openapitools.client.models.CategoryEntity


/**
 * Extension Function, die CategoryEntities zu CategorySelectItems mappt.
 * @author Freja Sender
 */
fun List<CategoryEntity>.toUnselectedCategorySelectItems() = map { cat ->
    CategorySelectItem(
        id = cat.id,
        label = cat.label,
        isChecked = false
    )
}