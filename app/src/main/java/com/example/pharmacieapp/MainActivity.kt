package com.example.pharmacieapp

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.pharmacieapp.DatabaseManagement.DataManager.recupererUtilisateurParId
import com.example.pharmacieapp.DatabaseManagement.DatabaseHelper
import com.example.pharmacieapp.FragmentAbout.AboutFragment
import com.example.pharmacieapp.FragmentEnvoyerOrdonnance.OrdonanceFragment
import com.example.pharmacieapp.FragmentHistory.HistoryFragment
import com.example.pharmacieapp.FragmentMap.MapFragment
import com.example.pharmacieapp.FragmentProfile.ProfileFragment
import com.example.pharmacieapp.classes.User
import com.example.pharmacieapp.databinding.ActivityMainBinding
import com.example.pharmacieapp.fragmentDeRecherche.RecherchePrincipaleFragment
import com.google.android.material.navigation.NavigationView
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navigationView: NavigationView
    private lateinit var databaseHelper: DatabaseHelper
    private var userId: Long? = null
    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = this.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val selectedLanguage = sharedPreferences.getString("LANGUAGE", "fr") ?: "fr"
        setLocale(this, selectedLanguage)

        // Initialisation de View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        databaseHelper = DatabaseHelper(this)

        userId = sharedPreferences.getLong("ID", -1)
        user = recupererUtilisateurParId(databaseHelper, userId!!)

        navigationView = binding.navigationView

        // Référencer les composants du header
        val headerView = navigationView.getHeaderView(0)
        val userName: TextView = headerView.findViewById(R.id.user_name)
        val userEmail: TextView = headerView.findViewById(R.id.user_email)
        val radioGroup: RadioGroup = binding.navigationView.getHeaderView(0).findViewById(R.id.radio_group)

        // Mettre à jour les boutons radio en fonction de la langue sélectionnée
        when (selectedLanguage) {
            "fr" -> radioGroup.check(R.id.radio_french)
            "ar" -> radioGroup.check(R.id.radio_arabic)
        }

        // Modifier les composants
        userName.text = "${user?.nom} ${user?.prenom}"
        userEmail.text = "${user?.email}"

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_french -> {
                    setLocale(this, "fr")
                }
                R.id.radio_arabic -> {
                    setLocale(this, "ar")
                }
            }
            recreate() // Recreate the activity to apply the new language
        }

        // Initialiser la Toolbar
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""  // Supprime le titre
        supportActionBar?.setDisplayShowTitleEnabled(false)  // Désactive l'affichage du titre

        // Gérer le clic sur iconMenu pour ouvrir/fermer le DrawerLayout
        binding.iconMenu.setOnClickListener {
            if (binding.main.isDrawerOpen(binding.navigationView)) {
                binding.main.closeDrawer(binding.navigationView) // Ferme si ouvert
            } else {
                binding.main.openDrawer(binding.navigationView) // Ouvre si fermé
            }
        }

        // Récupérer l'ID de l'utilisateur à partir de l'intent
        val userId = intent.getLongExtra("USER_ID", -1)

        // Toujours afficher le fragment de recherche par défaut
        if (userId != -1L) {
            val profileFragment = ProfileFragment().apply {
                arguments = Bundle().apply {
                    putLong("USER_ID", userId)
                }
            }
            val recherchePrincipaleFragment = RecherchePrincipaleFragment().apply {
                arguments = Bundle().apply {
                    putLong("USER_ID", userId)
                }
            }

            replaceFragment(recherchePrincipaleFragment)

            // Gérer les clics sur les éléments du menu
            binding.navigationView.setNavigationItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menu_profile -> replaceFragment(profileFragment)
                    R.id.menu_search_medicines -> replaceFragment(recherchePrincipaleFragment)
                    R.id.menu_about_us -> replaceFragment(AboutFragment())
                    R.id.menu_history -> replaceFragment(HistoryFragment())
                    R.id.menu_send_prescription -> replaceFragment(OrdonanceFragment())
                    R.id.menu_map -> replaceFragment(MapFragment())
                }
                binding.navigationView.setCheckedItem(menuItem.itemId)
                binding.main.closeDrawers()
                true
            }
        }
    }

    private fun setLocale(activity: Activity, langCode: String) {
        val locale = Locale(langCode)
        Locale.setDefault(locale)
        val configuration = activity.resources.configuration
        configuration.setLocale(locale)
        activity.resources.updateConfiguration(configuration, activity.resources.displayMetrics)

        // Enregistrer la langue sélectionnée dans SharedPreferences
        val sharedPreferences = activity.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("LANGUAGE", langCode).apply()
    }

    public fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
