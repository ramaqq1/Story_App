package com.ramaqq.storyapp_submission1.ui.maps

import android.content.Context
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.ramaqq.storyapp_submission1.R
import com.ramaqq.storyapp_submission1.data.response.ListStoryItem
import com.ramaqq.storyapp_submission1.databinding.FragmentMapsBinding
import com.ramaqq.storyapp_submission1.databinding.FragmentStoriesBinding
import com.ramaqq.storyapp_submission1.pojo.UserPreference
import java.io.IOException
import java.util.*

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class MapsFragment : Fragment(), OnMapReadyCallback {
    private lateinit var binding: FragmentMapsBinding
    private lateinit var mMap: GoogleMap
    private lateinit var pref: UserPreference
    private lateinit var viewModel: MapViewModel
    private var boundsBuilder = LatLngBounds.Builder()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pref = UserPreference.getInstance(requireActivity().dataStore)
        viewModel = ViewModelProvider(this)[MapViewModel::class.java]
        viewModel.init(pref)

//        viewModel.getLocation.observe(viewLifecycleOwner){
//            if (it != null)
//                setStoriesLocation(it)
//            else
//                Toast.makeText(requireContext(), "tidak ada lokasi", Toast.LENGTH_SHORT).show()
//        }

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)



    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        val jakarta = LatLng(-6.3, 106.82)
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(jakarta, 7f))

        viewModel.getLocation.observe(viewLifecycleOwner){
            if (it != null)
                setStoriesLocation(it)
        }
    }
    private fun setStoriesLocation(location: List<ListStoryItem>){
        location.forEach {
            val latLng = LatLng(it.lat, it.lon)
            val address = getAddressName(it.lat, it.lon)
            mMap.addMarker(MarkerOptions()
                .position(latLng)
                .title(it.name)
                .snippet(address))
        }
    }

    private fun getAddressName(lat: Double, lon: Double): String? {
        var addressName: String? = null
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        try {
            val list = geocoder.getFromLocation(lat, lon, 1)
            if (list != null && list.size != 0) {
                addressName = list[0].getAddressLine(0)
                Log.d("TAG", "getAddressName: $addressName")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return addressName
    }
}