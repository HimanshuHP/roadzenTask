package com.himanshuph.roadzentask.ui

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
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
import com.himanshuph.roadzentask.utils.toast
import kotlinx.android.synthetic.main.fragment_location.*
import java.util.*

class LocationFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMarkerDragListener {

    var needsInit: Boolean = false
    var latestPosition: LatLng? = null
    var timer: Timer? = null
    var count = -1;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return container?.inflate(R.layout.fragment_location)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFrag = fragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        if (savedInstanceState == null) {
            needsInit = true
        }

        mapFrag?.retainInstance = true
        mapFrag?.getMapAsync(this)
        decodeAddressBtn.setOnClickListener {
            proceedToShowAddress()
        }

        autoDecodeAddressBtn.setOnClickListener {
            scheduleDecoder()
        }
    }

    private fun proceedToShowAddress() {
        try {
            val (address, city) = getAddress()
            Toast.makeText(context, "Address: $address $city", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            e.printStackTrace();
        }
    }

    private fun getAddress(): Pair<String, String> {
        val geocoder = Geocoder(context.applicationContext, Locale.getDefault())
        val addresses: List<Address>? = geocoder.getFromLocation(latestPosition?.latitude
                ?: 0.0, latestPosition?.longitude
                ?: 0.0, 1)
        return if (addresses != null && addresses.isNotEmpty()) {
            val address = addresses[0].getAddressLine(0)
            val city = addresses[0].locality
            Pair(address, city)
        } else Pair("", "")
    }

    fun scheduleDecoder() {
        val handler = Handler();
        timer = Timer();
        val backtask = object : TimerTask() {
            override fun run() {
                handler.post {
                    try {
//                        Log.d("check", Thread.currentThread().toString());
                        count++;
                        proceedToShowAddress()
                        if (count == 5) {
                            timer?.cancel();
                            timer = null;
                        }
                    } catch (e: Exception) {
                    }
                }
            }
        };
        timer?.schedule(backtask, 0, 10000); //execute in every 20000 ms*/
    }

    override fun onMapReady(googleMap: GoogleMap) {
        if (needsInit) {
            latestPosition = LatLng(28.6441844, 77.1118256)
            val center = CameraUpdateFactory.newLatLng(LatLng(28.6441844, 77.1118256))
            val zoom = CameraUpdateFactory.zoomTo(12f)

            googleMap.moveCamera(center)
            googleMap.animateCamera(zoom)
            addMarker(googleMap, 28.6441844, 77.1118256)
        }
        googleMap.setOnInfoWindowClickListener(this)
        googleMap.setOnMarkerDragListener(this)
    }

    override fun onInfoWindowClick(marker: Marker?) {
        context.toast(marker?.title ?: "", Toast.LENGTH_LONG)
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

    override fun onDestroyView() {
        super.onDestroyView()

        timer?.cancel();
        timer = null;

    }


    companion object {

        @JvmField
        val TAG = LocationFragment::class.java.simpleName

        @JvmStatic
        fun newInstance() = LocationFragment()
    }
}
