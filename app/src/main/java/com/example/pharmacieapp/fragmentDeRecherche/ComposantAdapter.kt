package com.example.pharmacieapp.fragmentDeRecherche

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pharmacieapp.R
import com.example.pharmacieapp.classes.Composant

class ComposantAdapter(
    private val composantList: List<Composant>,
    private val context: Context
) : RecyclerView.Adapter<ComposantAdapter.ComposantViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComposantViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_composant, parent, false)
        return ComposantViewHolder(view)
    }

    override fun onBindViewHolder(holder: ComposantViewHolder, position: Int) {
        val composant: Composant = composantList[position]
        holder.composantName.text = composant.name

    }

    override fun getItemCount(): Int {
        return composantList.size
    }

    class ComposantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var composantName: TextView = itemView.findViewById(R.id.composantName)
    }
}