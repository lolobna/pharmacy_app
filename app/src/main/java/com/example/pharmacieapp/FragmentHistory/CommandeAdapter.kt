package com.example.pharmacieapp.FragmentHistory

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pharmacieapp.DatabaseManagement.DataManager.recupererMedicamentParId
import com.example.pharmacieapp.DatabaseManagement.DatabaseHelper
import com.example.pharmacieapp.R
import com.example.pharmacieapp.classes.Commande

class CommandeAdapter(
    private val orderList: List<Commande>,
    private val dbHelper: DatabaseHelper,
    private val context: Context
) : RecyclerView.Adapter<CommandeAdapter.OrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_commande, parent, false)
        return OrderViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orderList[position]
        Log.d("CommandeAdapter", "Binding order: $order")
        val medicament = recupererMedicamentParId(dbHelper, order.idMedicament)

        holder.medicamentNameTextView.text = medicament?.nom ?: "Unknown"
        holder.medicamentPriceTextView.text = "Price: ${medicament?.prix ?: "MAD"}"
        holder.quantityTextView.text = "Quantity: ${order.quantity}"
        holder.dateReservationTextView.text = "Date: ${order.DateReservation}"

        val imageResId = context.resources.getIdentifier(medicament?.image, "drawable", context.packageName)
        if (imageResId != 0) {
            holder.medicamentImageView.setImageResource(imageResId)
        } else {
            holder.medicamentImageView.setImageResource(R.drawable.ic_launcher_background) // Image par défaut
        }
    }

    override fun getItemCount(): Int = orderList.size

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val medicamentImageView: ImageView = itemView.findViewById(R.id.medicamentImageView)
        val medicamentNameTextView: TextView = itemView.findViewById(R.id.medicamentNameTextView)
        val medicamentPriceTextView: TextView = itemView.findViewById(R.id.medicamentPriceTextView)
        val quantityTextView: TextView = itemView.findViewById(R.id.quantityTextView)
        val dateReservationTextView: TextView = itemView.findViewById(R.id.dateReservationTextView)
    }
}
