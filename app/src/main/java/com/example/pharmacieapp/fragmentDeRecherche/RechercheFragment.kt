package com.example.pharmacieapp.fragmentDeRecherche

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pharmacieapp.DatabaseManagement.DataManager

import com.example.pharmacieapp.DatabaseManagement.DataManager.estTableMedicamentsVide

import com.example.pharmacieapp.DatabaseManagement.DataManager.insererMedicamentsParDefaut
import com.example.pharmacieapp.DatabaseManagement.DataManager.recupererCategoriesMedicaments
import com.example.pharmacieapp.DatabaseManagement.DataManager.recupererIdsMedicaments
import com.example.pharmacieapp.DatabaseManagement.DataManager.recupererTypesMedicaments
import com.example.pharmacieapp.DatabaseManagement.DataManager.supprimerTousLesMedicaments
import com.example.pharmacieapp.DatabaseManagement.DatabaseHelper
import com.example.pharmacieapp.R
import com.example.pharmacieapp.classes.Medicament
import com.example.pharmacieapp.databinding.ActivityMainBinding
import com.example.pharmacieapp.databinding.FragmentRechercheBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RechercheFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RechercheFragment : Fragment() {
    private lateinit var binding: FragmentRechercheBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchEditText: EditText
    private lateinit var medicamentAdapter: MedicamentAdapter
    private var medicamentsList: List<Medicament> = emptyList()
    private var filteredMedicamentsList: List<Medicament> = emptyList()
    private lateinit var spinnerType: Spinner
    private lateinit var spinnerCategorie: Spinner


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {




        binding = FragmentRechercheBinding.inflate(inflater, container, false)
        val view = binding.root


        recyclerView = binding.medicamentsRecyclerView
        searchEditText = binding.nomMedicamentRechercher
        spinnerType = binding.spinnerType
        spinnerCategorie = binding.spinnerCategorie

        val dbHelper = DatabaseHelper(requireContext())

        // Initialize spinners
        initializeSpinners(dbHelper)

        // Load and display medications
        loadMedicaments(dbHelper)

        // RecyclerView Adapter
        medicamentAdapter = MedicamentAdapter(filteredMedicamentsList) { medicament ->
            val action = RechercheFragmentDirections.actionRechercheFragmentToDetailFragment(medicament)
            view.findNavController().navigate(action)
        }
        recyclerView.adapter = medicamentAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Filter medications based on search text
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                chercherMedicaments(charSequence.toString().trim())
            }
            override fun afterTextChanged(editable: Editable?) {}
        })
        return view
    }

    private fun initializeSpinners(dbHelper: DatabaseHelper) {
        val types = mutableListOf(getString(R.string.none)) + recupererTypesMedicaments(dbHelper)
        val categories = mutableListOf(getString(R.string.none)) + recupererCategoriesMedicaments(dbHelper)

        val typeAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, types)
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerType.adapter = typeAdapter

        val categorieAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        categorieAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategorie.adapter = categorieAdapter

        spinnerType.setSelection(0)
        spinnerCategorie.setSelection(0)

        // Handle spinner selection changes
        spinnerType.onItemSelectedListener = createItemSelectedListener()
        spinnerCategorie.onItemSelectedListener = createItemSelectedListener()
    }

    private fun createItemSelectedListener(): AdapterView.OnItemSelectedListener {
        return object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedType = spinnerType.selectedItem.toString()
                val selectedCategorie = spinnerCategorie.selectedItem.toString()
                filterMedicaments(selectedType, selectedCategorie)
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                filterMedicaments()
            }
        }
    }

    private fun loadMedicaments(dbHelper: DatabaseHelper) {
        // If medication table is empty, insert default values
        supprimerTousLesMedicaments(dbHelper)
        if (estTableMedicamentsVide(dbHelper)) {
            insererMedicamentsParDefaut(dbHelper)
        }

        // Retrieve medications
        medicamentsList = DataManager.recupererMedicaments(dbHelper)
        filteredMedicamentsList = medicamentsList

    }

    private fun filterMedicaments(selectedType: String = "", selectedCategorie: String = "") {
        filteredMedicamentsList = medicamentsList.filter {
                    (selectedType == "Aucun" || it.type == selectedType) && // Filter by type
                    (selectedCategorie == "Aucun" || it.categorie == selectedCategorie) // Filter by
        }
        medicamentAdapter.updateList(filteredMedicamentsList)

    }

    private fun chercherMedicaments(query: String = "") {
        filteredMedicamentsList = medicamentsList.filter {
            (query.isEmpty() || it.nom.contains(query, ignoreCase = true))
        }
        medicamentAdapter.updateList(filteredMedicamentsList)

    }


}
