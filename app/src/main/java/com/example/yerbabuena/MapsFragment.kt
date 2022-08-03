package com.example.yerbabuena

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.location.LocationManager
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.location.*
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.firebase.auth.FirebaseAuth

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MapsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var lastLocation: Location
    private lateinit var currentLocation: Location
    private lateinit var mMap: GoogleMap
    private var zoom:Float = 14.0F

    private var pickUpMarker: Marker? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    /**
     * Actualiza la ubicacion con las nuevas coordenadas obtenidas por el sensor GPS
     */
    private val locationCallBack: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            val location: Location? = result?.lastLocation
            if(location != null) {
                val latLng = LatLng(location.latitude, location.longitude)
                updateLocation(latLng)
            }
        }}

    /**
     * Remueve el callback de actualizaciones para la obtencion de la ubicacion
     */
    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient.removeLocationUpdates(locationCallBack)
    }

    // Este Callback se dispara cuando el mapa este listo para su uso
    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        mMap = googleMap
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        zoom = googleMap.maxZoomLevel - 3F
        getCurrentLocation()
        googleMap.setOnCameraMoveListener{
            zoom = googleMap.cameraPosition.zoom
        }
    }

    /**
     * Muestra una peticion al usuario para que conceda los permisos de acceder a su ubicacion actual
     */
    private val permReqLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.entries.all {
                it.value
            }
            if (granted) {
                Toast.makeText(requireContext(), "Permiso concedido", Toast.LENGTH_SHORT).show()
                val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
                mapFragment?.getMapAsync(callback)
            }
            else
            {
                Toast.makeText(requireContext(), "Permiso denegado", Toast.LENGTH_SHORT).show()
            }
        }

    /**
     * Verifica que el usuario tenga habilitado el sensor GPS
     */
    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    // Getting last known location
    private fun getLastLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            getLocationPermission()
        }
        /*fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                // Got last known location. In some rare situations this can be null.
                if (location != null)
                {
                    val latLng: LatLng = LatLng(location.latitude, location.longitude)
                    updateLocation(latLng)
                }
            }*/
        val currentLocationRequest = CurrentLocationRequest.Builder().build()
        fusedLocationClient.getCurrentLocation(currentLocationRequest, null).addOnSuccessListener { location : Location? ->
            if (location != null)
            {
                val latLng = LatLng(location.latitude, location.longitude)
                updateLocation(latLng)
            }
        }
    }
    private  fun getCurrentLocation()
    {
        try {
            mMap?.isMyLocationEnabled = true
            val locationRequest = LocationRequest.create()
            locationRequest.priority =  Priority.PRIORITY_HIGH_ACCURACY
            locationRequest.interval = 3000
            //locationRequest.fastestInterval = 5000
            //locationRequest.numUpdates = 3
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallBack, null)
        }
        catch (ex:SecurityException)
        {
            Toast.makeText(requireContext(), ex.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (!isLocationEnabled())
        {
            var builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            var message:String = getString(R.string.msg_request_permission_rationale, getString(R.string.app_name))
            builder.setTitle(R.string.app_name).setMessage(message)
            builder.setPositiveButton("OK") { dialog, which ->
                dialog.dismiss()
                val intent1 = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent1)
            }
            builder.create().show()
        }
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            //ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            //    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
            Toast.makeText(requireContext(), "${R.string.msg_request_permission}", Toast.LENGTH_SHORT).show()
            permReqLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION))  {
            var builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            var message = getString(R.string.msg_request_permission_rationale, getString(R.string.app_name))
            builder.setTitle(getString(R.string.app_name)).setMessage(message)
            builder.setPositiveButton("OK") { dialog, _ ->
                permReqLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
                dialog.dismiss()
            }
            builder.create().show()
        } else {
            val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
            mapFragment?.getMapAsync(callback)
        }
    }

    private fun updateLocation(latLng: LatLng) {
        var name = FirebaseAuth.getInstance().currentUser?.displayName
        if (name == null) name = "Cliente"
        mMap?.clear()
        val markerOptions:MarkerOptions = MarkerOptions()
        markerOptions.title("$name")
        markerOptions.icon(bitmapDescriptorFromVector(requireContext(), R.drawable.ic_delivery_man))
        markerOptions.draggable(true)
        markerOptions.position(latLng)
        mMap?.addMarker(markerOptions)
        mMap?.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getLocationPermission()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment fragment_menu.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MapsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            this.setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }
}

