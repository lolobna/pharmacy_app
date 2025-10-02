package com.example.pharmacieapp.fragmentDeRecherche

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import com.example.pharmacieapp.R
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pharmacieapp.DatabaseManagement.DataManager
import com.example.pharmacieapp.DatabaseManagement.DataManager.recupererComposantsParId
import com.example.pharmacieapp.DatabaseManagement.DataManager.recupererIdMedicamentParNom
import com.example.pharmacieapp.DatabaseManagement.DatabaseHelper
import com.example.pharmacieapp.classes.Composant
import com.example.pharmacieapp.databinding.FragmentDetailBinding

// TODO: Remove unused parameters and imports if necessary

class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    private val args: DetailFragmentArgs by navArgs()  // Get arguments passed to the fragment
    private lateinit var databaseHelper: DatabaseHelper

    @SuppressLint("SetTextI18n", "DiscouragedApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Initialize the binding object
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        databaseHelper = DatabaseHelper(requireContext())
        val view = binding.root

        // Configuration du bouton de retour
        val btnRetour : ImageView = binding.btnRetour // Assurez-vous que vous avez ajouté l'ID dans le binding
        btnRetour.setOnClickListener {
            view.findNavController().navigateUp() // Navigue vers la page précédente
        }


        // Get the 'Medicament' object passed as argument
        val medicament = args.Medicament


        // Remplir les TextViews avec les informations du médicament
        binding.medicamentNom.text = "${medicament.nom}"
        binding.medicamentType.text = "${binding.medicamentType.text} : ${medicament.type}"
        Log.d("image", "${medicament.image}")

        // If medicament.image is a String (name of the image), convert it to a drawable resource ID
        val imageResId = resources.getIdentifier(medicament.image, "drawable", requireContext().packageName)
        if (imageResId != 0) {
            binding.medicamentImage.setImageResource(imageResId)
        } else {
            binding.medicamentImage.setImageResource(R.drawable.erreur)  // Image par défaut
        }

        binding.medicamentDescription.text = "${binding.medicamentDescription.text} : ${medicament.description}"

        val composants = recupererComposantsParId(databaseHelper, medicament.id)


        val composantList = composants.map { Composant(it.trim()) }
        val recyclerViewComposants: RecyclerView = binding.recyclerViewComposants
        recyclerViewComposants.layoutManager = LinearLayoutManager(requireContext())
        val adapter = ComposantAdapter(composantList, requireContext())
        recyclerViewComposants.adapter = adapter

        binding.medicamentCategorie.text = "${binding.medicamentCategorie.text} : ${medicament.categorie}"
        binding.medicamentPrix.text = "${binding.medicamentPrix.text} ${medicament.prix}MAD"

        // Set up the "Reserver" button
        binding.Reserver.setOnClickListener {
            val action = DetailFragmentDirections.actionDetailFragmentToReservationFragment(medicament)
            view?.findNavController()?.navigate(action)
        }

        // Access the database to fetch additional information
        val dbHelper = DatabaseHelper(requireContext())
        val medicamentId = recupererIdMedicamentParNom(dbHelper, medicament.nom)
        Log.d("idMedicament", "$medicamentId")

        // Display the medication advice in the TextView
        binding.medicamentConseils.text = " ${medicament.conseils}"

        // Return the root view of the binding
        return view
    }

}
