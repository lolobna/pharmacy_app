package com.example.pharmacieapp.FragmentHistory



import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pharmacieapp.R
import com.example.pharmacieapp.classes.Ordonnance
import com.squareup.picasso.Picasso

class OrdonnanceAdapter(private val ordonnances: List<Ordonnance>, private val context: Context) :
    RecyclerView.Adapter<OrdonnanceAdapter.OrdonnanceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdonnanceViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_ordonnance, parent, false)
        return OrdonnanceViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: OrdonnanceViewHolder, position: Int) {
        val ordonnance = ordonnances[position]
        holder.textViewDate.text = ordonnance.dateOrdonnance

        val imageUri = Uri.parse(ordonnance.ordonnanceImage)
        Log.d("OrdonnanceImage", "URI de l'image: $imageUri")
        Picasso.get().load(imageUri).placeholder(R.drawable.placeholder) // Assurez-vous d'avoir une image placeholder
            .error(R.drawable.erreur).into(holder.imageViewPreview)
    }

    override fun getItemCount(): Int = ordonnances.size

    class OrdonnanceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewPreview: ImageView = itemView.findViewById(R.id.imageViewPreview)
        val textViewDate: TextView = itemView.findViewById(R.id.textViewDate)
    }
}
