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
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.pottytime.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.dialog_layout.*
import kotlinx.android.synthetic.main.dialog_layout.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlin.reflect.typeOf

class HomeFragment : Fragment(), OnMapReadyCallback {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var lastLocation: Location
    private lateinit var mMap: GoogleMap

    private var isMen: Boolean = true
    private var isNeutral: Boolean = true
    private var isWomen: Boolean = true
    private var isFamily: Boolean = true
    private var isHandicap: Boolean = true
    private var isSatellite: Boolean = true

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

        //BOTTOM MODAL SHEET CODE
        rootView.homeButton.setOnClickListener {
            val dialog = BottomSheetDialog(this.requireContext())
            val view = layoutInflater.inflate(R.layout.dialog_layout,null)
            setInitState(view)
            setButtonPreferences(view)
            dialog.setContentView(view)
            dialog.show()
        }




        return rootView
    }
    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap!!
        mMap.clear()
        if (isSatellite) mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID) else mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL)
        val schoolBounds = LatLngBounds(LatLng(34.061334,-118.174004),
            LatLng(34.071732,-118.162157)
        )
        addMapMarkers()
        mMap.setLatLngBoundsForCameraTarget(schoolBounds)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(schoolBounds.center,20.0f))
        mMap.setMinZoomPreference(16.0f)
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

    private fun filterDB():MutableList<Query>{
        val queryList = mutableListOf<Query>()
        val bathroomRef = db.collection("bathrooms")
        if(isFamily) queryList.add(bathroomRef.whereEqualTo("isFamily",true))
        if(isNeutral) queryList.add(bathroomRef.whereEqualTo("gender","Neutral"))
        if(isHandicap) queryList.add(bathroomRef.whereEqualTo("isHandicap",true))
        if(isMen) queryList.add(bathroomRef.whereEqualTo("gender","Male"))
        if(isWomen) queryList.add(bathroomRef.whereEqualTo("gender","Female"))

        return queryList

    }

    private fun setInitState(rootView: View){
        if(isMen) rootView.menButton.setImageResource(R.drawable.male_on) else rootView.menButton.setImageResource(R.drawable.male_off)
        if(isWomen) rootView.womenButton.setImageResource(R.drawable.female_on) else rootView.womenButton.setImageResource(R.drawable.female_off)
        if(isFamily) rootView.familyButton.setImageResource(R.drawable.family_on) else rootView.familyButton.setImageResource(R.drawable.family_off)
        if(isHandicap) rootView.handicapButton.setImageResource(R.drawable.handicap_on) else rootView.handicapButton.setImageResource(R.drawable.handicap_off)
        if(isNeutral) rootView.neutralButton.setImageResource(R.drawable.neutral_on) else rootView.neutralButton.setImageResource(R.drawable.neutral_off)
    }

    private fun setButtonPreferences(rootView: View){

        rootView.menButton.setOnClickListener{
            if(isMen) rootView.menButton.setImageResource(R.drawable.male_off) else rootView.menButton.setImageResource(R.drawable.male_on)
            isMen = !isMen
            mMap.clear()
            addMapMarkers()
        }
        rootView.womenButton.setOnClickListener{
            if(isWomen) rootView.womenButton.setImageResource(R.drawable.female_off) else rootView.womenButton.setImageResource(R.drawable.female_on)
            isWomen = !isWomen
            mMap.clear()
            addMapMarkers()
        }
        rootView.familyButton.setOnClickListener{
            if(isFamily) rootView.familyButton.setImageResource(R.drawable.family_off) else rootView.familyButton.setImageResource(R.drawable.family_on)
            isFamily = !isFamily
            mMap.clear()
            addMapMarkers()
        }
        rootView.handicapButton.setOnClickListener{
            if(isHandicap) rootView.handicapButton.setImageResource(R.drawable.handicap_off) else rootView.handicapButton.setImageResource(R.drawable.handicap_on)
            isHandicap = !isHandicap
            mMap.clear()
            addMapMarkers()
        }
        rootView.neutralButton.setOnClickListener{
            if(isNeutral) rootView.neutralButton.setImageResource(R.drawable.neutral_off) else rootView.neutralButton.setImageResource(R.drawable.neutral_on)
            isNeutral = !isNeutral
            mMap.clear()
            addMapMarkers()
        }
        rootView.mapSwitch.setOnCheckedChangeListener{buttonView, isChecked ->
            if(isChecked) isSatellite = false else isSatellite = !isSatellite
            onMapReady(mMap)
        }

    }

    private fun addMapMarkers(){
        val queryList:MutableList<Query> = filterDB()
        for(query in queryList){
            query.get().addOnSuccessListener { result ->
                for(document in result){
                    val lat = document.data.get("lat").toString().toDouble()
                    val lon = document.data.get("lon").toString().toDouble()
                    val coord = LatLng(lat,lon)
//                    val marker: MarkerOptions = MarkerOptions().position(coord).title("bathroom")
//                    marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.toilet_map))
//                    mMap.addMarker(marker)
                    mMap.addMarker(MarkerOptions().position(coord).title("bathroom").snippet("sadfasdfasdfsan "))
                }
            }
        }
    }

}