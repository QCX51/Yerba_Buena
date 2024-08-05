package com.example.yerbabuena


import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException

import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.location.*
import com.google.android.gms.maps.SupportMapFragment
import com.google.firebase.auth.FirebaseAuth

class UbicacionFragment : Fragment() {

    private var fusedLocationClient: FusedLocationProviderClient? = null
    private lateinit var locationRequest: LocationRequest.Builder
    private lateinit var googleMap: GoogleMap
    private var zoom:Float = 14.0F

    /**
     * Actualiza la ubicacion con las nuevas coordenadas obtenidas por el sensor GPS
     */
    private val locationCallBack: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            val location: Location? = result.lastLocation
            if(location != null) {
                val fragment = activity?.supportFragmentManager?.findFragmentByTag("PedidosFragment") as PedidosFragment
                fragment.buttonEnable()
                val latLng = LatLng(location.latitude, location.longitude)
                updateLocation(latLng)
            }
        }}

    /**
     * Remueve el callback de actualizaciones: LocationCallback
     */
    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient?.removeLocationUpdates(locationCallBack)
    }

    /**
     * Este Callback se dispara una vez que el mapa este listo para su uso
     */
    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        this.googleMap = googleMap
        googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        zoom = googleMap.maxZoomLevel - 3F
        getCurrentLocationUpdates()
    }
    /**
     *
     */
    private val resolutionForResult =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { activityResult ->
            if (activityResult.resultCode == RESULT_OK) {
                try {
                    fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
                    fusedLocationClient?.requestLocationUpdates(locationRequest.build(), locationCallBack, null)
                    Toast.makeText(requireContext(), "GPS Enabled", Toast.LENGTH_SHORT).show()
                } catch (ex: SecurityException) {
                    Toast.makeText(requireContext(), ex.message, Toast.LENGTH_SHORT).show()
                }
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
            else {
                Toast.makeText(requireContext(), "Permiso denegado", Toast.LENGTH_SHORT).show()
                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION))  {
                    val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
                    val message = getString(R.string.msg_request_permission_rationale, getString(R.string.app_name))
                    builder.setTitle(getString(R.string.app_name)).setMessage(message)
                    builder.setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                    }
                    //Show permission explanation dialog...
                    builder.create().show()
                } else {
                    //Never ask again selected, or device policy prohibits the app from having that permission.
                    //So, disable that feature, or fall back to another situation...
                }
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

    /**
     * Obtiene la ultima ubicacion conocida,
     * si no exieste alguna, hace una peticion para obtener la ultima ubicacion mas reciente
     */
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
        val currentLocationRequest = CurrentLocationRequest.Builder()
            .setDurationMillis(10000) // 10 Milliseconds
            .setMaxUpdateAgeMillis(0) // Only freshly derived locations
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .build()

        fusedLocationClient?.getCurrentLocation(currentLocationRequest, null)?.addOnSuccessListener { location : Location? ->
            if (location != null)
            {
                val latLng = LatLng(location.latitude, location.longitude)
                updateLocation(latLng)
            }
        }
    }

    /**
     * hace una peticion para obtener la ubicacion mas reciente en un tiempo determidad en un numero
     * determinado de veces
     */
    private  fun getCurrentLocation()
    {
        try {
            locationRequest = LocationRequest.Builder(3000)
            locationRequest.setMaxUpdates(3)
            locationRequest.setDurationMillis(3000)
            locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY)

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
            fusedLocationClient?.requestLocationUpdates(locationRequest.build(), locationCallBack, null)
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
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            //ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            //    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
            Toast.makeText(requireContext(), "${R.string.msg_request_permission}", Toast.LENGTH_SHORT).show()
            permReqLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
        } else {
            val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
            mapFragment?.getMapAsync(callback)
        }
    }

    private fun updateLocation(latLng: LatLng) {
        var name = FirebaseAuth.getInstance().currentUser?.displayName
        if (name == null) name = "Cliente"
        googleMap.clear()
        try {
            googleMap.isMyLocationEnabled = false
            googleMap.isTrafficEnabled = false
            googleMap.isIndoorEnabled = false
            googleMap.isBuildingsEnabled = false
        } catch (ex: SecurityException)
        {}
        val markerOptions = MarkerOptions()
        markerOptions.title("$name")
        markerOptions.draggable(true)
        markerOptions.position(latLng)
        googleMap.addMarker(markerOptions)
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*val key = arguments?.get("key") as String
        Toast.makeText(requireContext(), key, Toast.LENGTH_SHORT).show()*/
        getLocationPermission()
    }

    private fun getCurrentLocationUpdates()
    {
        val builder = LocationSettingsRequest.Builder()
        builder.setAlwaysShow(true)
        locationRequest = LocationRequest.Builder(3000)
        locationRequest.setMaxUpdates(3)
        locationRequest.setDurationMillis(3000)
        locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY)

        builder.addLocationRequest(locationRequest.build())

        val result = LocationServices.getSettingsClient(requireActivity())
        result.checkLocationSettings(builder.build()).addOnCompleteListener {
            try {
                // All location settings are satisfied. The client can initialize location
                // requests here.
                it.getResult(ApiException::class.java)
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
                fusedLocationClient?.requestLocationUpdates(locationRequest.build(), locationCallBack, null)
                Toast.makeText(requireContext(), "GPS Enabled", Toast.LENGTH_SHORT).show()
            } catch (ex: ApiException) {
                when (ex.statusCode)
                {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        // Location settings are not satisfied. But could be fixed by showing the
                        // user a dialog.
                        val resolvable = ex as ResolvableApiException
                        val intentSenderRequest = IntentSenderRequest.Builder(resolvable.resolution).build()
                        resolutionForResult.launch(intentSenderRequest)
                        Toast.makeText(requireContext(), "GPS Enable Request", Toast.LENGTH_SHORT).show()
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                    }
                }
            } catch (ex: SecurityException)
            {
                Toast.makeText(requireContext(), ex.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}