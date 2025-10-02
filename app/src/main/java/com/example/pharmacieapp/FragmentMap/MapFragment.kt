package com.example.pharmacieapp.FragmentMap


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pharmacieapp.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var map: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_map, container, false)

        // Initialiser la carte
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        return view
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        // Liste des emplacements des pharmacies à Safi
        val pharmacies = listOf(
            LatLng(32.2654, -9.2376), // Pharmacie Al Amal
            LatLng(32.2635, -9.2388), // Pharmacie Yassine
            LatLng(32.2668, -9.2363)   // Pharmacie Badr
        )

        // Ajouter des marqueurs pour chaque pharmacie
        map.addMarker(MarkerOptions().position(pharmacies[0]).title("Pharmacie LoubnAya"))
        map.addMarker(MarkerOptions().position(pharmacies[1]).title("Pharmacie LoubnAya"))
        map.addMarker(MarkerOptions().position(pharmacies[2]).title("Pharmacie LoubnAya"))

        // Déplacer la caméra sur la première pharmacie
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(pharmacies[0], 15f))
    }
}