package de.fantjastisch.cards_frontend.category

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.compose.CategorySelect
import de.fantjastisch.categorys_frontend.category.CategoryGraphFragment
import org.openapitools.client.models.CategoryEntity
import kotlin.properties.Delegates


class CategoryChooserDialogFragment : DialogFragment() {

    // wenn die categories gesetzt werden und der view schon existiert, so setzen wir die categories in den view
    var categories: List<MultiSelectionList.Item>
            by Delegates.observable(emptyList()) { _, _, newItems ->
                Log.v("MultiList", "Setting items $newItems")
                subCategoriesList?.items = newItems
            }

    // wenn der view gesetzt wird, so setzen wir auch die categories in den view
    private var subCategoriesList: MultiSelectionList?
            by Delegates.observable(null) { _, _, newView ->
                Log.v("MultiList", "Setting categories ${categories.size} to $newView")
                newView?.items = categories
            }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

//        val outerView = inflater.inflate(R.layout.layout_with_save, container, false)
//        val inputContainer = outerView.findViewById<FrameLayout>(R.id.input_content_container)
//        inflater.inflate(R.layout.create_category_fragment, inputContainer, true)
//        return outerView
        return ComposeView(requireContext()).apply { setContent { CategorySelect() } }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val saveButton = view.findViewById<Button>(R.id.save_button)
        val labelInput = view.findViewById<TextInputEditText>(R.id.category_label_textinput)
        subCategoriesList = view.findViewById<MultiSelectionList>(R.id.category_subcategories_list)
        saveButton.setOnClickListener {
            (parentFragment as CategoryGraphFragment).onCreateClicked(
                    label = labelInput.text.toString()
            )
        }
        (subCategoriesList as MultiSelectionList).items = categories
        super.onViewCreated(view, savedInstanceState)
    }

    fun showCategories(categories: List<CategoryEntity>) {

    }

    override fun onStart() {
        super.onStart()
    }

    fun showError(error: String) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
    }

    fun showSuccess(category: CategoryEntity) {
        Toast.makeText(requireContext(), category.toString(), Toast.LENGTH_LONG).show()
    }

}