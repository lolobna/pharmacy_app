package com.example.pharmacieapp.fragmentDeRecherche

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.compose.ui.geometry.times
import androidx.compose.ui.unit.times
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.pharmacieapp.R
import com.example.pharmacieapp.databinding.FragmentReservationBinding
import com.example.pharmacieapp.DatabaseManagement.DataManager // Importez votre DataManager ici
import com.example.pharmacieapp.DatabaseManagement.DataManager.getUserOrders
import com.example.pharmacieapp.DatabaseManagement.DataManager.recupererIdMedicamentParNom
import com.example.pharmacieapp.DatabaseManagement.DatabaseHelper // Importez votre DatabaseHelper ici
import com.example.pharmacieapp.classes.Medicament
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.properties.Delegates

class ReservationFragment : Fragment() {
    private lateinit var binding: FragmentReservationBinding
    private val args: ReservationFragmentArgs by navArgs()
    private lateinit var dbHelper: DatabaseHelper
    private var userId: Long? = null
    private var availableQuantity by Delegates.notNull<Int>() // Variable pour stocker la quantité disponible

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReservationBinding.inflate(inflater, container, false)
        dbHelper = DatabaseHelper(requireContext())

        val view = binding.root

        // Configuration du bouton de retour
        val btnRetour: ImageView = binding.btnRetour
        btnRetour.setOnClickListener {
            view.findNavController().navigateUp()
        }

        val sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        userId = sharedPreferences.getLong("ID", 0)

        Log.d("Reservation id", "User ID: $userId")

        val medicament = args.Medicament
        availableQuantity = medicament.quantity // Assurez-vous que la quantité est stockée dans l'objet Medicament

        val imageResId = resources.getIdentifier(medicament.image, "drawable", requireContext().packageName)
        if (imageResId != 0) {
            binding.ImageMedicament.setImageResource(imageResId)
        } else {
            binding.ImageMedicament.setImageResource(R.drawable.ic_launcher_background)
        }

        binding.NomMedicament.text = "${binding.NomMedicament.text}  ${medicament.nom}"
        binding.prixMedicament.text = "${binding.prixMedicament.text}  ${medicament.prix}MAD"

        // Initialiser les valeurs par défaut
        binding.editTextQuantite.setText("1")
        binding.total.text =  getString(R.string.votre_total) +" : ${medicament.prix} MAD"

        // Mettre à jour le total quand la quantité change
        binding.editTextQuantite.setOnEditorActionListener { _, _, _ ->
            updateTotal(medicament.prix.toFloat())
            false
        }

        binding.btnConfirmerReservation.setOnClickListener {
            val quantiteChoisie = binding.editTextQuantite.text.toString().toIntOrNull() ?: 1
            if (quantiteChoisie > availableQuantity) {
                Toast.makeText(requireContext(), "Quantité choisie supérieure à la quantité disponible.", Toast.LENGTH_SHORT).show()
            } else {
                showConfirmationDialog(medicament, recupererIdMedicamentParNom(dbHelper, medicament.nom)!!, userId!!)
            }
        }

        return view
    }

    private fun saveReservation(MedicamentID: Long, UserId: Long) {
        if (userId == 0L) {
            Toast.makeText(requireContext(), "Utilisateur non valide.", Toast.LENGTH_SHORT).show()
            return
        }

        val quantite = binding.editTextQuantite.text.toString().toInt()
        val dateReservation = System.currentTimeMillis()
        val dateFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val formattedDate = dateFormatter.format(dateReservation)

        // Appel de la méthode pour insérer la réservation dans la base de données
        DataManager.insertReservation(dbHelper, MedicamentID, UserId, quantite, formattedDate)

        // Mettre à jour la quantité de médicaments
        updateMedicamentQuantity(MedicamentID, quantite)

        // Afficher un message de confirmation
        Toast.makeText(requireContext(), getString(R.string.reservation_success), Toast.LENGTH_SHORT).show()

    }

    private fun updateMedicamentQuantity(medicamentId: Long, quantiteRéservée: Int) {
        // Récupérer la quantité actuelle dans la base de données
        val currentQuantity = DataManager.getCurrentQuantity(dbHelper, medicamentId) // Assurez-vous d'avoir cette méthode

        // Calculer la nouvelle quantité
        val newQuantity = currentQuantity - quantiteRéservée

        // Mettre à jour la quantité dans la base de données
        DataManager.updateMedicamentQuantity(dbHelper, medicamentId, newQuantity) // Assurez-vous d'avoir cette méthode
    }

    private fun updateTotal(prixDuMedicament: Float) {
        val quantite = binding.editTextQuantite.text.toString().toIntOrNull() ?: 1
        val total = prixDuMedicament * quantite
        binding.total.text = getString(R.string.votre_total) + " : $total MAD"


    }

    private fun showConfirmationDialog(medicament: Medicament ,MedicamentID : Long,  UserId: Long) {
        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.confirmation_reservation_title))
            .setMessage(getString(R.string.confirmation_reservation_message, binding.editTextQuantite.text, medicament.nom))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                saveReservation(MedicamentID, UserId)
            }
            .setNegativeButton(getString(R.string.no), null)
            .create()

        alertDialog.show()
    }




    companion object {
        @JvmStatic
        fun newInstance() = ReservationFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dbHelper.close()
    }

}