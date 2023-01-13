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
        val answerInput = view.findViewById<TextInputEditText>(R.id.card_answer_textinput)
        val questionInput = view.findViewById<TextInputEditText>(R.id.card_question_textinput)
        saveButton.setOnClickListener {
            this.presenter.onSaveClicked(
                answer = answerInput.text.toString(),
                question = questionInput.text.toString(),
                tag = "I"
            )
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
}