package com.example.pharmacieapp.FragmentProfile

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pharmacieapp.DatabaseManagement.DataManager
import com.example.pharmacieapp.DatabaseManagement.DatabaseHelper
import com.example.pharmacieapp.FragmentEnvoyerOrdonnance.OrdonanceFragment
import com.example.pharmacieapp.MainActivity
import com.example.pharmacieapp.R
import com.example.pharmacieapp.databinding.FragmentProfileBinding
import com.example.pharmacieapp.fragmentDeRecherche.RecherchePrincipaleFragment

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ProfileFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var databaseHelper: DatabaseHelper
    private var userId: Long? = null // ID de l'utilisateur connecté

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        databaseHelper = DatabaseHelper(requireContext())
        userId = arguments?.getLong("USER_ID") // Récupération de l'ID utilisateur
        Log.d("userIdProfile", "$userId")
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        // Configuration des écouteurs de clic
        binding.Medicament.setOnClickListener {
            // Remplacez le fragment avec MedicamentFragment
            (activity as MainActivity).replaceFragment(RecherchePrincipaleFragment())
        }

        binding.Ordonnace.setOnClickListener {
            // Remplacez le fragment avec OrdonanceFragment
            (activity as MainActivity).replaceFragment(OrdonanceFragment())
        }

        userId?.let {
            val utilisateur = DataManager.recupererUtilisateurParId(databaseHelper, it)
            utilisateur?.let { user ->
                binding.nameTextView.text = "${user.nom} ${user.prenom}"
                binding.emailTextView.text = user.email
                binding.numeroTextView.text = user.numero
            } ?: run {
                // Gérer le cas où l'utilisateur n'est pas trouvé
                binding.nameTextView.text = "Utilisateur non trouvé"
                binding.emailTextView.text = ""
            }
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
