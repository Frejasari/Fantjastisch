package de.fantjastisch.cards_frontend.card

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import de.fantjastisch.cards.R
import de.fantjastisch.cards_frontend.category.CategoryChooserDialogFragment
import de.fantjastisch.cards_frontend.category.MultiSelectionList
import org.openapitools.client.models.CardEntity

class GlossaryFragment : Fragment() {
    private lateinit var presenter: GlossaryPresenter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val outerView = inflater.inflate(R.layout.layout_with_save, container, false)
        val inputContainer = outerView.findViewById<FrameLayout>(R.id.input_content_container)
        inflater.inflate(R.layout.create_card_fragment, inputContainer, true)
        return outerView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val saveButton = view.findViewById<Button>(R.id.save_button)
        val selectCategoryButton = view.findViewById<Button>(R.id.card_category_select_button)
        val answerInput = view.findViewById<TextInputEditText>(R.id.card_answer_textinput)
        val questionInput = view.findViewById<TextInputEditText>(R.id.card_question_textinput)
        saveButton.setOnClickListener {
            this.presenter.onSaveClicked(
                    answer = answerInput.text.toString(),
                    question = questionInput.text.toString(),
                    tag = "I"
            )
        }
        selectCategoryButton.setOnClickListener {
            presenter.onSelectCategoriesClicked()

        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        this.presenter = GlossaryPresenter(this)
        super.onStart()
    }

    fun showError(error: String) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
    }

    fun showSucces(card: CardEntity) {
        Toast.makeText(requireContext(), card.toString(), Toast.LENGTH_LONG).show()
    }

    fun showCategoriesDialog(categories: List<MultiSelectionList.Item>) {
        val categoryChooserDialogFragment = CategoryChooserDialogFragment()
        categoryChooserDialogFragment.categories = categories
        requireActivity().supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                        R.animator.fragment_enter,
                        R.animator.fragment_exit,
                        R.animator.fragment_enter,
                        R.animator.fragment_exit,
                )
                .add(android.R.id.content, categoryChooserDialogFragment, "categoryChooserDialog")
                .addToBackStack("categoryChooserDialog")
                .commit()
//        categoryChooserDialogFragment
//                .show(childFragmentManager, "categoryChooserDialog")

    }
}