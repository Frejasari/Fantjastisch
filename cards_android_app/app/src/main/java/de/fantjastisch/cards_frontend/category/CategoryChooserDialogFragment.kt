package de.fantjastisch.categorys_frontend.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import de.fantjastisch.cards.R
import org.openapitools.client.models.CategoryEntity

class CreateCategoryDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val outerView = inflater.inflate(R.layout.layout_with_save, container, false)
        outerView.layoutParams = (
                outerView.layoutParams
                    ?: LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
                ).apply {
                height = LayoutParams.WRAP_CONTENT
            }
        val inputContainer = outerView.findViewById<FrameLayout>(R.id.input_content_container)
        inflater.inflate(R.layout.create_category_fragment, inputContainer, true)
        return outerView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val saveButton = view.findViewById<Button>(R.id.save_button)
        val labelInput = view.findViewById<TextInputEditText>(R.id.category_label_textinput)
        saveButton.setOnClickListener {
            (parentFragment as CategoryGraphFragment).onCreateClicked(
                label = labelInput.text.toString()
            )
        }
        super.onViewCreated(view, savedInstanceState)
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