package com.example.pharmacieapp.FragmentLogin

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.pharmacieapp.DatabaseManagement.DataManager
import com.example.pharmacieapp.DatabaseManagement.DatabaseHelper
import com.example.pharmacieapp.MainActivity
import com.example.pharmacieapp.R
import com.example.pharmacieapp.classes.User
import com.example.pharmacieapp.databinding.FragmentRegisterBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class RegisterFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var databaseHelper: DatabaseHelper
    private val countryCodes = mapOf(
        "USA" to Pair("+1", 10),
        "France" to Pair("+33", 9),
        "Maroc" to Pair("+212", 9),
        "Allemagne" to Pair("+49", 9),
        "Royaume-Uni" to Pair("+44", 10),
        "Canada" to Pair("+1", 10),
        "Espagne" to Pair("+34", 9),
        "Italie" to Pair("+39", 9),
        "Belgique" to Pair("+32", 9),
        "Tunisie" to Pair("+216", 8),
        "Algérie" to Pair("+213", 9),
        "Portugal" to Pair("+351", 9)
    )

    private lateinit var selectedCountryCode: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        databaseHelper = DatabaseHelper(requireContext())
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view = binding.root

        // Initialiser le préfixe par défaut
        selectedCountryCode = "+212"

        // Configuration de l'adaptateur pour le Spinner
        val countries = countryCodes.keys.toList()
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, countries)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.countrySpinner.adapter = adapter

        binding.countrySpinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val countryName = countries[position]
                selectedCountryCode = countryCodes[countryName]!!.first
                val expectedLength = countryCodes[countryName]!!.second

                // Mettre à jour le hint avec le préfixe et les underscores
                binding.numeroEditText.hint = "$selectedCountryCode " + "_".repeat(expectedLength)
                binding.numeroEditText.setText("") // Effacez le numéro local
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                selectedCountryCode = "+212" // Valeur par défaut
                binding.numeroEditText.hint = "$selectedCountryCode " + "_".repeat(9) // Valeur par défaut pour le Maroc
            }
        })

        binding.registerButton.setOnClickListener {
            val nom = binding.nomEditText.text.toString().trim()
            val prenom = binding.prenomEditText.text.toString().trim()
            val numero = binding.numeroEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            // Validation des entrées
            if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Veuillez remplir tous les champs.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!isValidEmail(email)) {
                Toast.makeText(requireContext(), "Veuillez entrer un email valide.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!isValidPhoneNumber(numero)) {
                Toast.makeText(requireContext(), "Veuillez entrer un numéro de téléphone valide.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!isValidPassword(password)) {
                Toast.makeText(requireContext(), "Le mot de passe doit contenir au moins 6 caractères.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = User(0, nom, prenom, email, password, selectedCountryCode + numero, "")

            val insertionResult = DataManager.insertUtilisateur(databaseHelper, user)
            Log.d("register", "$insertionResult ")

            if (insertionResult != -1L) {
                Toast.makeText(requireContext(), "Inscription réussie !", Toast.LENGTH_SHORT).show()

                val userId = DataManager.getUserIdByEmail(databaseHelper, email)

                val sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                sharedPreferences.edit()
                    .putLong("ID", userId ?: -1)
                    .apply()

                val intent = Intent(requireContext(), MainActivity::class.java).apply {
                    putExtra("USER_ID", userId ?: -1)
                }
                startActivity(intent)
                requireActivity().finish()
            } else {
                Toast.makeText(requireContext(), "Utilisateur déjà existant.", Toast.LENGTH_SHORT).show()
            }

            Log.d("register", "Insertion result: $insertionResult")
        }

        return view
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPhoneNumber(phone: String): Boolean {
        val expectedLength = countryCodes.entries.find { it.value.first == selectedCountryCode }?.value?.second
        return phone.length == (expectedLength ?: 0) && phone.all { it.isDigit() }
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegisterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
