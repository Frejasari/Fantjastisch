package de.fantjastisch.categorys_frontend.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import de.fantjastisch.cards.R
import org.openapitools.client.models.CategoryEntity

class CategoryGraphFragment : DialogFragment() {
    private var categories: List<CategoryEntity> = listOf()
    private lateinit var presenter: CategoryPresenter

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val text = itemView.findViewById<TextView>(R.id.category_label)
        fun bind(category: CategoryEntity) {
            text.text = category.label
        }
    }

    val adapter = object : RecyclerView.Adapter<CategoryViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.categorie_recycler_view_row, parent, false)
            return CategoryViewHolder(view)

        }

        override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
            holder.bind(categories[position])
        }

        override fun getItemCount(): Int {
            return categories.size
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.category_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.categories_recycler)

        recyclerView.adapter = adapter

        val createButton = view.findViewById<Button>(R.id.create_category_button)
        createButton.setOnClickListener {
            CreateCategoryDialogFragment().show(
                childFragmentManager,
                "createCategoryChild"
            )
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        this.presenter = CategoryPresenter(this)
        super.onStart()
    }

    fun showError(error: String) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
    }

    fun showSuccess(category: CategoryEntity) {
        Toast.makeText(requireContext(), category.toString(), Toast.LENGTH_LONG).show()
    }

    fun showCategories(categories: List<CategoryEntity>) {
        this.categories = categories
        // dem recycler view bescheid gaben, dass Daten sich geändert haben.
        adapter.notifyDataSetChanged()
    }

    fun onCreateClicked(label: String) {
        this.presenter.onSaveClicked(label = label)
    }

    fun closeDialog() {
//        as? - safe cast, falls null
//        ?.callsomething -> nur wenn nicht null
        (childFragmentManager.findFragmentByTag("createCategoryChild") as? DialogFragment)?.dismiss()
    }

}