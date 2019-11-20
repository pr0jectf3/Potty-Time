package com.example.pottytime.ui.home

import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.pottytime.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.reflect.typeOf

class HomeFragment : Fragment(), OnMapReadyCallback {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var lastLocation: Location
    private lateinit var mMap: GoogleMap
    val db = FirebaseFirestore.getInstance()

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

//            val binding = DataBindingUtil.inflate<FragmentGoogleMapsBinding>(
//                    inflater,R.layout.fragment_google_maps,container,false)
        var rootView = inflater.inflate(R.layout.fragment_home, container, false)

        val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)

        return rootView
    }
    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap!!
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID)
        val schoolBounds = LatLngBounds(LatLng(34.061334,-118.174004),
            LatLng(34.071732,-118.162157)
        )

        db.collection("bathrooms")
            .get().addOnSuccessListener { result ->
                for(document in result){
                    val lat = document.data.get("lat").toString().toDouble()
                    val lon = document.data.get("lon").toString().toDouble()
                    val coord = LatLng(lat,lon)
                    //Log.d(TAG, "DocumentSnapshot data: ${document.data.get("lat") is Double}")
                    mMap.addMarker(MarkerOptions().position(coord).title("bathroom"))
                }
            }
        val sydney = LatLng(40.73, -73.99)
        //mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,12.0f))
        mMap.setLatLngBoundsForCameraTarget(schoolBounds)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(schoolBounds.center,20.0f))
        mMap.setMinZoomPreference(18.0f)
        setUpMap()
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this.requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
    }

}