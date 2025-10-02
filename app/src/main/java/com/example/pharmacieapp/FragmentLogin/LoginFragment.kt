package com.example.pharmacieapp.FragmentLogin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.pharmacieapp.DatabaseManagement.DataManager
import com.example.pharmacieapp.DatabaseManagement.DataManager.getUserIdByEmail
import com.example.pharmacieapp.DatabaseManagement.DatabaseHelper
import com.example.pharmacieapp.MainActivity
import com.example.pharmacieapp.R
import com.example.pharmacieapp.databinding.FragmentLoginBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class LoginFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        databaseHelper = DatabaseHelper(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (checkUserCredentials(email, password)) {
                Toast.makeText(requireContext(), "connection réussie !", Toast.LENGTH_SHORT).show()

                val userId = getUserIdByEmail(databaseHelper, email) // Récupérer l'ID de l'utilisateur

                val sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putLong("ID", userId ?: 0)
                editor.apply()

                // Démarrer MainActivity et passer l'ID de l'utilisateur
                val intent = Intent(requireContext(), MainActivity::class.java).apply {
                    putExtra("USER_ID", userId ?: -1)
                }
                startActivity(intent)
                requireActivity().finish() // Optionnel : fermer l'activité de connexion
            } else {
                Toast.makeText(requireContext(), "Identifiants incorrects.", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun checkUserCredentials(email: String, password: String): Boolean {
        return DataManager.checkUser(databaseHelper, email, password)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
