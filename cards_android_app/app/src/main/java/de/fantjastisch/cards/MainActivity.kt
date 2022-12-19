package de.fantjastisch.cards

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import de.fantjastisch.cards.card.Card
import de.fantjastisch.cards.card.CardRepository
import de.fantjastisch.cards.card.generateCardId
import de.fantjastisch.cards.validation.ValidationException
import java.util.UUID

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

class MyFirstFragment: Fragment() {
    private lateinit var presenter: MyFirstPresenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val outerView = inflater.inflate(R.layout.layout_with_save, container, false)
        val inputContainer = outerView.findViewById<FrameLayout>(R.id.input_content_container)
        inflater.inflate(R.layout.create_card_fragment, inputContainer , true)
        return outerView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val saveButton = view.findViewById<Button>(R.id.save_button)
        val answerInput = view.findViewById<TextInputEditText>(R.id.card_answer_textinput)
        val questionInput = view.findViewById<TextInputEditText>(R.id.card_question_textinput)
        saveButton.setOnClickListener{
            this.presenter.onSaveClicked(answer = answerInput.text.toString(), question = questionInput.text.toString())
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        this.presenter = MyFirstPresenter(this)
        super.onStart()
    }

    fun showError(error: String) {
        TODO("Not yet implemented")
    }
}

class MyFirstPresenter(private val view: MyFirstFragment){

    private val repo = CardRepository(AppDatabase.database.cardDao())

    fun onSaveClicked(answer: String, question: String, id: UUID? =null){
       try {
           validate(answer, question, id)
           repo.insert(Card(question=question, answer=answer, id = id?: generateCardId()))
       } catch (ex: ValidationException){
           ex.errors.forEach { error -> view.showError(error) }
       }
    }

    private fun validate(answer: String?, question: String?, id: UUID?){
        val errors = mutableListOf<String>()
        if(question.isNullOrBlank()){
            errors.add("question is empty");
        }
        if(answer.isNullOrBlank()){
            errors.add("answer is empty");
        }
        if(errors.isNotEmpty()){
            throw ValidationException(errors)
        }
    }

}
