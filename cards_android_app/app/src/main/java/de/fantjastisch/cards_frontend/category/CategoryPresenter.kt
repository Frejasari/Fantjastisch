package de.fantjastisch.categorys_frontend.category

import de.fantjastisch.cards_frontend.validation.ValidationException
import org.openapitools.client.models.CategoryEntity
import org.openapitools.client.models.CreateCategoryEntity
import java.util.*

class CategoryPresenter(private val view: CategoryGraphFragment) {

    private val repo = CategoryRepository()

    init {
        getPage()
    }


    fun onSaveClicked(label: String, id: UUID? = null) {
        validate(label, id)
        repo.createCategory(
            CreateCategoryEntity(
                label = label,
                subCategories = listOf()
            ),
            {
                view.closeDialog()
                getPage()
            },
            { view.showError("Something went wrong") }
        )

    }

    fun getPage() {
        repo.getPage({ view.showCategories(it) }, { view.showError("not found") });
    }

    private fun validate(label: String?, id: UUID?) {
        val errors = mutableListOf<String>()
        if (label.isNullOrBlank()) {
            errors.add("label is empty");
        }
        if (errors.isNotEmpty()) {
            throw ValidationException(errors)
        }
    }
}