package com.example.pharmacieapp.fragmentDeRecherche

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pharmacieapp.R
import com.example.pharmacieapp.classes.Medicament

class MedicamentAdapter(private var medicaments: List<Medicament>, private val onItemClick: (Medicament) -> Unit) :
    RecyclerView.Adapter<MedicamentAdapter.MedicamentViewHolder>() {

    class MedicamentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomMedicament: TextView = itemView.findViewById(R.id.nomMedicament)
        val medicamentImage: ImageView = itemView.findViewById(R.id.medicamentImage)
        val descriptionMedicament: TextView = itemView.findViewById(R.id.descriptionMedicament)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicamentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_medicament, parent, false)
        return MedicamentViewHolder(view)
    }

    override fun onBindViewHolder(holder: MedicamentViewHolder, position: Int) {
        val medicament = medicaments[position]
        holder.nomMedicament.text = medicament.nom
        holder.descriptionMedicament.text = medicament.description

        // Utilisation de resources.getIdentifier pour charger l'image par son nom
        val imageResId = holder.itemView.context.resources.getIdentifier(medicament.image, "drawable", holder.itemView.context.packageName)

        if (imageResId != 0) {
            holder.medicamentImage.setImageResource(imageResId)
        } else {
            // Afficher une image par défaut si l'ID de la ressource n'est pas trouvé
            holder.medicamentImage.setImageResource(R.drawable.ic_launcher_background)
        }

        holder.itemView.setOnClickListener { onItemClick(medicament) }
    }

    override fun getItemCount(): Int = medicaments.size

    fun updateList(newList: List<Medicament>) {
        medicaments = newList
        notifyDataSetChanged()
    }
}