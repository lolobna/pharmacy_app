package com.example.pharmacieapp.FragmentHistory

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pharmacieapp.DatabaseManagement.DataManager.getUserOrders
import com.example.pharmacieapp.DatabaseManagement.DataManager.getUserOrdonnances
import com.example.pharmacieapp.DatabaseManagement.DatabaseHelper
import com.example.pharmacieapp.R
import com.example.pharmacieapp.databinding.FragmentHistoryBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HistoryFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

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
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val view = binding.root

        if (userId != -1L) {
            val orders = getUserOrders(databaseHelper, userId!!)
            Log.d("orders", "${orders}")
            Log.d("orders size", "Fetched orders: ${orders.size}")

            binding.CommandesList.layoutManager = LinearLayoutManager(requireContext())
            binding.CommandesList.adapter = CommandeAdapter(orders, databaseHelper, requireContext())

            val ordonnances = getUserOrdonnances(databaseHelper, userId!!)
            Log.d("ordonnances", "${ordonnances}")
            Log.d("ordonnances size", "Fetched orders: ${ordonnances.size}")

            binding.recyclerViewOrdonnances.layoutManager = LinearLayoutManager(requireContext())
            binding.recyclerViewOrdonnances.adapter = OrdonnanceAdapter(ordonnances, requireContext())

        } else {
            Log.e("HistoryFragment", "User ID not found")
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HistoryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
