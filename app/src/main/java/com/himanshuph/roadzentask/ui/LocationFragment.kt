package com.himanshuph.roadzentask.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.IBinder
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.himanshuph.roadzentask.R
import com.himanshuph.roadzentask.service.DecoderService
import com.himanshuph.roadzentask.service.ServiceCallback
import com.himanshuph.roadzentask.utils.inflate
import com.himanshuph.roadzentask.utils.toast
import kotlinx.android.synthetic.main.fragment_location.*
import java.util.*


class LocationFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMarkerDragListener, ServiceCallback {

    var needsInit: Boolean = false
    var latestPosition: LatLng? = null
    var decoderService:DecoderService?=null
    var isBound: Boolean = false

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
            disableBtn()
            decoderService?.startTimerTask()
        }
    }

    override fun enableBtn() {
        autoDecodeAddressBtn.isEnabled = true
    }

    fun disableBtn() {
        autoDecodeAddressBtn.isEnabled = false
    }

    override fun onStart() {
        super.onStart()
        val intent = Intent(context, DecoderService::class.java)
        context.bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
    }

    val mConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            if(decoderService==null) {
                decoderService = (service as DecoderService.LocalBinder).decoderService
                decoderService?.setCallbacks(this@LocationFragment)
            }
            isBound = true
        }

        override fun onServiceDisconnected(className: ComponentName) {
            isBound = false
        }
    }

    override fun decodeAddress() {
        proceedToShowAddress()
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

    fun stopService() {
        if (isBound) {
            decoderService?.setCallbacks(null) // unregister
            context.unbindService(mConnection)
            isBound = false;
        }
    }

    override fun onStop() {
        super.onStop()
        stopService()
    }


    companion object {

        @JvmField
        val TAG = LocationFragment::class.java.simpleName

        @JvmStatic
        fun newInstance() = LocationFragment()
    }
}
