package com.example.pharmacieapp.FragmentEnvoyerOrdonnance

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.pharmacieapp.DatabaseManagement.DataManager
import com.example.pharmacieapp.DatabaseManagement.DatabaseHelper
import com.example.pharmacieapp.R
import com.example.pharmacieapp.classes.Ordonnance
import com.example.pharmacieapp.databinding.FragmentOrdonanceBinding
import com.squareup.picasso.Picasso
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val PERMISSIONS_REQUEST_CODE = 1001

class OrdonanceFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentOrdonanceBinding? = null
    private val binding get() = _binding!!

    private lateinit var imageUri: Uri
    private lateinit var databaseHelper: DatabaseHelper
    private var userId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        databaseHelper = DatabaseHelper(requireContext())
        val sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        userId = sharedPreferences.getLong("ID", -1)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrdonanceBinding.inflate(inflater, container, false)
        val view = binding.root

        // Masquer le bouton "Envoyer ordonnance" par défaut
        binding.buttonSendOrdonnance.visibility = View.GONE

        // Choisir une image depuis la galerie
        binding.buttonGallery.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_IMAGE_GALLERY)
        }

        // Capturer une image avec la caméra
        binding.buttonCamera.setOnClickListener {
            if (arePermissionsGranted()) {
                openCamera() // Ouvrir la caméra si les permissions sont accordées
            } else {
                checkPermissions() // Vérifier les permissions si elles ne sont pas accordées
            }
        }

        // Envoyer ordonnance
        binding.buttonSendOrdonnance.setOnClickListener {
            showConfirmationDialog()
        }

        return view
    }

    private fun checkPermissions() {
        val permissionsRequired = arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        val permissionsNotGranted = permissionsRequired.filter {
            ContextCompat.checkSelfPermission(requireContext(), it) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsNotGranted.isNotEmpty()) {
            requestPermissions(permissionsNotGranted.toTypedArray(), PERMISSIONS_REQUEST_CODE)
        } else {
            Toast.makeText(requireContext(), "Permissions déjà accordées.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun arePermissionsGranted(): Boolean {
        return ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoFile = createImageFile()
        photoFile?.also {
            imageUri = FileProvider.getUriForFile(
                requireContext(),
                "${requireContext().packageName}.fileprovider",
                it
            )
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            startActivityForResult(intent, REQUEST_IMAGE_CAMERA)
        } ?: run {
            Toast.makeText(requireContext(), "Échec de la création du fichier image", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                    // Permissions granted, open the camera
                    openCamera()
                } else {
                    Toast.makeText(requireContext(), "Permissions nécessaires non accordées", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_GALLERY -> {
                    val selectedImageUri = data?.data
                    // Utiliser Picasso pour charger l'image
                    Picasso.get().load(selectedImageUri).into(binding.imageViewPreview)
                    imageUri = selectedImageUri!!
                }
                REQUEST_IMAGE_CAMERA -> {
                    // Utiliser Picasso pour charger l'image capturée
                    Picasso.get().load(imageUri).into(binding.imageViewPreview)
                }
            }
            // Afficher le bouton "Envoyer ordonnance" après avoir sélectionné une image
            binding.buttonSendOrdonnance.visibility = View.VISIBLE
        }
    }

    private fun createImageFile(): File? {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = requireContext().getExternalFilesDir(null)
        return File.createTempFile("ORDONNANCE_${timeStamp}", ".jpg", storageDir)
    }

    private fun showConfirmationDialog() {
        AlertDialog.Builder(requireContext()).apply {
            setTitle("Confirmation")
            setMessage("Voulez-vous envoyer cette ordonnance ?")
            setPositiveButton("Oui") { _, _ ->
                val dateOrdonnance = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                val ordonnance = Ordonnance(
                    userId = userId!!,
                    ordonnanceImage = imageUri.toString(),
                    dateOrdonnance = dateOrdonnance
                )

                val newRowId = DataManager.saveOrdonnance(databaseHelper, ordonnance)
                if (newRowId == -1L) {
                    Toast.makeText(requireContext(), "Erreur lors de l'envoi de l'ordonnance", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Ordonnance envoyée avec succès", Toast.LENGTH_SHORT).show()
                }
            }
            setNegativeButton("Non") { dialog, _ -> dialog.dismiss() }
        }.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val REQUEST_IMAGE_GALLERY = 1
        private const val REQUEST_IMAGE_CAMERA = 2

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OrdonanceFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
