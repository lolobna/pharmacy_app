package com.example.pharmacieapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.pharmacieapp.DatabaseManagement.DataManager.recupererMedicaments
import com.example.pharmacieapp.DatabaseManagement.DataManager.recupererUtilisateurs
import com.example.pharmacieapp.DatabaseManagement.DatabaseHelper
import com.example.pharmacieapp.FragmentLogin.LoginFragment
import com.example.pharmacieapp.FragmentLogin.RegisterFragment
import com.example.pharmacieapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private var sharedPreferences: SharedPreferences? = null
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        databaseHelper = DatabaseHelper(this)

        // Vérifier si l'utilisateur est déjà connecté
        /*
        if (isUserLoggedIn) {
            showLoginFragment()
        } else {
            showLoginFragment()
        }
        */

        Log.d("all users", "${recupererUtilisateurs(databaseHelper)} ")
        Log.d("all medicament", "${recupererMedicaments(databaseHelper)} ")

        showLoginFragment()

        // Référencer les boutons
        binding.loginButton.setOnClickListener { showLoginFragment() }
        binding.registerButton.setOnClickListener { showRegisterFragment() }
    }

    private val isUserLoggedIn: Boolean
        get() = sharedPreferences!!.getBoolean(IS_LOGGED_IN, false)

    private fun showLoginFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, LoginFragment())
            .commit()
    }

    private fun showRegisterFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, RegisterFragment())
            .commit()
    }

    companion object {
        private const val PREF_NAME = "UserPrefs"
        private const val IS_LOGGED_IN = "isLoggedIn"
    }
}
