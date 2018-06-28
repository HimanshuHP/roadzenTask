package com.himanshuph.roadzentask.ui

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.himanshuph.roadzentask.R
import com.himanshuph.roadzentask.utils.inflate
import kotlinx.android.synthetic.main.fragment_location.*
import java.util.*

class LocationFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMarkerDragListener {

    var needsInit: Boolean = false
    var latestPosition : LatLng? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return container?.inflate(R.layout.fragment_location)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFrag = fragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        if (savedInstanceState == null) {
            needsInit = true
        }

        mapFrag.retainInstance = true
        mapFrag.getMapAsync(this)
        decodeAddressBtn.setOnClickListener {
            val geocoder = Geocoder(context.applicationContext, Locale.getDefault())


            val addresses: List<Address>
            try {
                addresses = geocoder.getFromLocation(latestPosition?.latitude ?: 0.0, latestPosition?.longitude
                        ?: 0.0, 1)
                val address = addresses[0].getAddressLine(0)
                val city = addresses[0].locality

                Toast.makeText(context, "Address: " +
                        address + " " + city, Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                e.printStackTrace();
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        if (needsInit) {
            latestPosition = LatLng(28.6441844, 77.1118256)
            val center = CameraUpdateFactory.newLatLng(LatLng(28.6441844, 77.1118256))
            val zoom = CameraUpdateFactory.zoomTo(15f)

            googleMap.moveCamera(center)
            googleMap.animateCamera(zoom)
            addMarker(googleMap, 28.6441844, 77.1118256)
        }
        googleMap.setOnInfoWindowClickListener(this)
        googleMap.setOnMarkerDragListener(this)
    }

    /*    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }*/

    override fun onInfoWindowClick(marker: Marker?) {
        Toast.makeText(context, marker?.title, Toast.LENGTH_LONG).show()
    }

    override fun onMarkerDragEnd(marker: Marker?) {
        val position = marker?.getPosition()
        latestPosition = position
        marker?.title = "${position?.latitude}, ${position?.longitude}"
        marker?.showInfoWindow()
        Log.d(javaClass.simpleName, String.format("Dragged to %f:%f",
                position?.latitude,
                position?.longitude))
    }

    override fun onMarkerDragStart(marker: Marker?) {

    }

    override fun onMarkerDrag(marker: Marker?) {
        marker?.hideInfoWindow()
    }

    private fun addMarker(map: GoogleMap, lat: Double, lon: Double) {
        map.addMarker(MarkerOptions().position(LatLng(lat, lon))
                .title("$lat,${lon}")
                .draggable(true))


    }

    override fun onDetach() {
        super.onDetach()

    }


    companion object {
        @JvmStatic
        fun newInstance() = LocationFragment()
    }
}
