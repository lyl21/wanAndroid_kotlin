//package github.lyl21.wanandroid.moudle.map
//
//import android.Manifest
//import android.annotation.SuppressLint
//import android.content.DialogInterface
//import android.content.pm.PackageManager
//import android.location.Location
//import android.os.Bundle
//import android.util.Log
//import android.view.KeyEvent
//import android.view.View
//import android.widget.EditText
//import android.widget.FrameLayout
//import android.widget.TextView
//import androidx.appcompat.app.AlertDialog
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import com.google.android.gms.location.FusedLocationProviderClient
//import com.google.android.gms.location.LocationServices
//import com.google.android.gms.maps.CameraUpdateFactory
//import com.google.android.gms.maps.GoogleMap
//import com.google.android.gms.maps.GoogleMap.OnMapClickListener
//import com.google.android.gms.maps.OnMapReadyCallback
//import com.google.android.gms.maps.SupportMapFragment
//import com.google.android.gms.maps.model.CameraPosition
//import com.google.android.gms.maps.model.LatLng
//import com.google.android.gms.maps.model.Marker
//import com.google.android.gms.maps.model.MarkerOptions
//import com.google.android.libraries.places.api.Places
//import com.google.android.libraries.places.api.model.Place
//import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
//import com.google.android.libraries.places.api.net.PlacesClient
//import github.lyl21.wanandroid.R
//import android.widget.Toast
//import androidx.recyclerview.widget.RecyclerView
//
//import com.google.android.gms.maps.model.BitmapDescriptorFactory
//import github.lyl21.wanandroid.adapter.LocationAdapter
//import java.lang.StringBuilder
//import okhttp3.OkHttpClient
//import okhttp3.Request
//import okhttp3.Response
//
//
//class LocationActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnCameraIdleListener {
//    private var map: GoogleMap? = null
//    private var cameraPosition: CameraPosition? = null
//
//    // The entry point to the Places API.
//    private lateinit var placesClient: PlacesClient
//
//    // The entry point to the Fused Location Provider.
//    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
//
//    // A default location (Sydney, Australia) and default zoom to use when location permission is
//    // not granted.
//    private val defaultLocation = LatLng(39.95895316640668, 116.52169489108084)
//    private var locationPermissionGranted = false
//
//    // The geographical location where the device is currently located. That is, the last-known
//    // location retrieved by the Fused Location Provider.
//    private var lastKnownLocation: Location? = null
//    private var likelyPlaceNames: Array<String?> = arrayOfNulls(0)
//    private var likelyPlaceAddresses: Array<String?> = arrayOfNulls(0)
//    private var likelyPlaceAttributions: Array<List<*>?> = arrayOfNulls(0)
//    private var likelyPlaceLatLngs: Array<LatLng?> = arrayOfNulls(0)
//
//    private lateinit var et_search: EditText
//    private lateinit var tv_search: TextView
//    private lateinit var rv_location: RecyclerView
//    private lateinit var locationAdapter: LocationAdapter
//
//
//    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
//        TODO("Not yet implemented")
//    }
//
//    // [START maps_current_place_on_create]
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        // [START_EXCLUDE silent]
//        // Retrieve location and camera position from saved instance state.
//        // [START maps_current_place_on_create_save_instance_state]
//        if (savedInstanceState != null) {
//            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION)
//            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION)
//        }
//        // [END maps_current_place_on_create_save_instance_state]
//        // [END_EXCLUDE]
//
//        // Retrieve the content view that renders the map.
//        setContentView(R.layout.activity_location)
//
//        // [START_EXCLUDE silent]
//        // Construct a PlacesClient
//        Places.initialize(applicationContext, MAP_KEY)
//        placesClient = Places.createClient(this)
//
//        // Construct a FusedLocationProviderClient.
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
//
//        // Build the map.
//        // [START maps_current_place_map_fragment]
//        val mapFragment = supportFragmentManager
//            .findFragmentById(R.id.map) as SupportMapFragment?
//        mapFragment?.getMapAsync(this)
//        // [END maps_current_place_map_fragment]
//        // [END_EXCLUDE]
//
//        et_search=findViewById(R.id.et_search)
//        tv_search=findViewById(R.id.tv_search)
//        rv_location=findViewById(R.id.rv_location)
//        locationAdapter= LocationAdapter()
//    }
//
//
//    /**
//     * Saves the state of the map when the activity is paused.
//     */
//    // [START maps_current_place_on_save_instance_state]
//    override fun onSaveInstanceState(outState: Bundle) {
//        map?.let { map ->
//            outState.putParcelable(KEY_CAMERA_POSITION, map.cameraPosition)
//            outState.putParcelable(KEY_LOCATION, lastKnownLocation)
//        }
//        super.onSaveInstanceState(outState)
//    }
//    // [END maps_current_place_on_save_instance_state]
//
//
//    /**
//     * Manipulates the map when it's available.
//     * This callback is triggered when the map is ready to be used.
//     */
//    // [START maps_current_place_on_map_ready]
//    override fun onMapReady(map: GoogleMap) {
//        this.map = map
//
//        // [START_EXCLUDE]
//        // [START map_current_place_set_info_window_adapter]
//        // Use a custom info window adapter to handle multiple lines of text in the
//        // info window contents.
////        this.map?.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
////            // Return null here, so that getInfoContents() is called next.
////            override fun getInfoWindow(arg0: Marker): View? {
////                return null
////            }
////
////            override fun getInfoContents(marker: Marker): View {
////                // Inflate the layouts for the info window, title and snippet.
////                val infoWindow = layoutInflater.inflate(
////                    R.layout.custom_info_contents,
////                    findViewById<FrameLayout>(R.id.map), false
////                )
////                val title = infoWindow.findViewById<TextView>(R.id.title)
////                title.text = marker.title
////                val snippet = infoWindow.findViewById<TextView>(R.id.snippet)
////                snippet.text = marker.snippet
////                return infoWindow
////            }
////        })
//        // [END map_current_place_set_info_window_adapter]
//
//        // Prompt the user for permission.
//        getLocationPermission()
//        // [END_EXCLUDE]
//
//        // Turn on the My Location layer and the related control on the map.
//        updateLocationUI()
//
//        // Get the current location of the device and set the position of the map.
//        getDeviceLocation()
//    }
//    // [END maps_current_place_on_map_ready]
//
//    /**
//     * Gets the current location of the device, and positions the map's camera.
//     */
//    // [START maps_current_place_get_device_location]
//    @SuppressLint("MissingPermission")
//    private fun getDeviceLocation() {
//        /*
//         * Get the best and most recent location of the device, which may be null in rare
//         * cases when a location is not available.
//         */
//        try {
//            if (locationPermissionGranted) {
//                val locationResult = fusedLocationProviderClient.lastLocation
//                locationResult.addOnCompleteListener(this) { task ->
//                    if (task.isSuccessful) {
//                        // Set the map's camera position to the current location of the device.
//                        lastKnownLocation = task.result
//                        if (lastKnownLocation != null) {
//                            map?.moveCamera(
//                                CameraUpdateFactory.newLatLngZoom(
//                                    LatLng(
//                                        lastKnownLocation!!.latitude,
//                                        lastKnownLocation!!.longitude
//                                    ), DEFAULT_ZOOM.toFloat()
//                                )
//                            )
//                        }
//                    } else {
//                        Log.d(TAG, "Current location is null. Using defaults.")
//                        Log.e(TAG, "Exception: %s", task.exception)
//                        map?.moveCamera(
//                            CameraUpdateFactory
//                                .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat())
//                        )
//                        map?.uiSettings?.isMyLocationButtonEnabled = false
//                    }
//                }
//            }
//        } catch (e: SecurityException) {
//            Log.e("Exception: %s", e.message, e)
//        }
//    }
//    // [END maps_current_place_get_device_location]
//
//    /**
//     * Prompts the user for permission to use the device location.
//     */
//    // [START maps_current_place_location_permission]
//    private fun getLocationPermission() {
//        /*
//         * Request location permission, so that we can get the location of the
//         * device. The result of the permission request is handled by a callback,
//         * onRequestPermissionsResult.
//         */
//        if (ContextCompat.checkSelfPermission(
//                this.applicationContext,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            )
//            == PackageManager.PERMISSION_GRANTED
//        ) {
//            locationPermissionGranted = true
//        } else {
//            ActivityCompat.requestPermissions(
//                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
//            )
//        }
//    }
//    // [END maps_current_place_location_permission]
//
//    /**
//     * Handles the result of the request for location permissions.
//     */
//    // [START maps_current_place_on_request_permissions_result]
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        locationPermissionGranted = false
//        when (requestCode) {
//            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
//
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.isNotEmpty() &&
//                    grantResults[0] == PackageManager.PERMISSION_GRANTED
//                ) {
//                    locationPermissionGranted = true
//                }
//            }
//        }
//        updateLocationUI()
//    }
//    // [END maps_current_place_on_request_permissions_result]
//
//    /**
//     * Prompts the user to select the current place from a list of likely places, and shows the
//     * current place on the map - provided the user has granted location permission.
//     */
//    // [START maps_current_place_show_current_place]
//    @SuppressLint("MissingPermission")
//    private fun showCurrentPlace() {
//        if (map == null) {
//            return
//        }
//        if (locationPermissionGranted) {
//            // Use fields to define the data types to return.
//            val placeFields = listOf(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)
//
//            // Use the builder to create a FindCurrentPlaceRequest.
//            val request = FindCurrentPlaceRequest.newInstance(placeFields)
//
//            // Get the likely places - that is, the businesses and other points of interest that
//            // are the best match for the device's current location.
//            val placeResult = placesClient.findCurrentPlace(request)
//            placeResult.addOnCompleteListener { task ->
//                if (task.isSuccessful && task.result != null) {
//                    val likelyPlaces = task.result
//                    likelyPlaces.placeLikelihoods.size
//                    var i = 0
//                    likelyPlaceNames = arrayOfNulls(10)
//                    likelyPlaceAddresses = arrayOfNulls(10)
//                    likelyPlaceAttributions = arrayOfNulls<List<*>?>(10)
//                    likelyPlaceLatLngs = arrayOfNulls(10)
//                    for (placeLikelihood in likelyPlaces?.placeLikelihoods ?: emptyList()) {
//                        // Build a list of likely places to show the user.
//                        likelyPlaceNames[i] = placeLikelihood.place.name
//                        likelyPlaceAddresses[i] = placeLikelihood.place.address
//                        likelyPlaceAttributions[i] = placeLikelihood.place.attributions
//                        likelyPlaceLatLngs[i] = placeLikelihood.place.latLng
//                        i++
//                        if (i > 10 - 1) {
//                            break
//                        }
//                    }
//
//                    // Show a dialog offering the user the list of likely places, and add a
//                    // marker at the selected place.
//                    openPlacesDialog()
//                } else {
//                    Log.e(TAG, "Exception: %s", task.exception)
//                }
//            }
//        } else {
//            // The user has not granted permission.
//            Log.i(TAG, "The user did not grant location permission.")
//
//            // Add a default marker, because the user hasn't selected a place.
//            map?.addMarker(
//                MarkerOptions()
//                    .title(getString(R.string.default_info_title))
//                    .position(defaultLocation)
//                    .snippet(getString(R.string.default_info_snippet))
//            )
//
//            // Prompt the user for permission.
//            getLocationPermission()
//        }
//    }
//    // [END maps_current_place_show_current_place]
//
//    /**
//     * Displays a form allowing the user to select a place from a list of likely places.
//     */
//    // [START maps_current_place_open_places_dialog]
//    private fun openPlacesDialog() {
//        // Ask the user to choose the place where they are now.
//        val listener =
//            DialogInterface.OnClickListener { dialog, which -> // The "which" argument contains the position of the selected item.
//                val markerLatLng = likelyPlaceLatLngs[which]
//                var markerSnippet = likelyPlaceAddresses[which]
//                if (likelyPlaceAttributions[which] != null) {
//                    markerSnippet = """
//                    $markerSnippet
//                    ${likelyPlaceAttributions[which]}
//                    """.trimIndent()
//                }
//
//                if (markerLatLng == null) {
//                    return@OnClickListener
//                }
//
//                // Add a marker for the selected place, with an info window
//                // showing information about that place.
//                map?.addMarker(
//                    MarkerOptions()
//                        .title(likelyPlaceNames[which])
//                        .position(markerLatLng)
//                        .snippet(markerSnippet)
//                )
//
//                // Position the map's camera at the location of the marker.
//                map?.moveCamera(
//                    CameraUpdateFactory.newLatLngZoom(
//                        markerLatLng,
//                        DEFAULT_ZOOM.toFloat()
//                    )
//                )
//            }
//
//        // Display the dialog.
//        AlertDialog.Builder(this)
//            .setTitle(R.string.pick_place)
//            .setItems(likelyPlaceNames, listener)
//            .show()
//    }
//    // [END maps_current_place_open_places_dialog]
//
//    /**
//     * Updates the map's UI settings based on whether the user has granted location permission.
//     */
//    // [START maps_current_place_update_location_ui]
//    @SuppressLint("MissingPermission")
//    private fun updateLocationUI() {
//        if (map == null) {
//            return
//        }
//        try {
//            if (locationPermissionGranted) {
//                map?.isMyLocationEnabled = true
//                map?.uiSettings?.isMyLocationButtonEnabled = true
//
//                map?.setOnMapClickListener(OnMapClickListener { latLng ->
//                    setCurrLocation(latLng)
//                })
//
//            } else {
//                map?.isMyLocationEnabled = false
//                map?.uiSettings?.isMyLocationButtonEnabled = false
//                lastKnownLocation = null
//                getLocationPermission()
//            }
//        } catch (e: SecurityException) {
//            Log.e("Exception: %s", e.message, e)
//        }
//    }
//
//    private fun setCurrLocation(latLng: LatLng) {
//        map?.clear()
//        if (marker != null) {
//            marker.remove()
//        }
//
//        val markerOptions = MarkerOptions()
//        markerOptions.position(latLng)
//        markerOptions.title("Current Position")
//        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
//        marker = map?.addMarker(markerOptions)!!
////        drawMarkers(latLng.latitude,latLng.longitude)
//        //move map camera
//        map?.moveCamera(CameraUpdateFactory.newLatLng(latLng))
//        map?.animateCamera(CameraUpdateFactory.zoomTo(11f))
//        getNearbyPlaces("", latLng.latitude, latLng.longitude, DEFAULT_RADIUS, "")
//        Toast.makeText(
//            this,
//            String.format("latitude:%.3f longitude:%.3f", latLng.latitude, latLng.longitude),
//            Toast.LENGTH_LONG
//        ).show()
//    }
//
//
//    companion object {
//        private val TAG = this::class.java.simpleName
//        private const val DEFAULT_ZOOM = 15
//        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
//
//        // Keys for storing activity state.
//        // [START maps_current_place_state_keys]
//        private const val KEY_CAMERA_POSITION = "camera_position"
//        private const val KEY_LOCATION = "location"
//        // [END maps_current_place_state_keys]
//
//        private const val MAP_KEY = "AIzaSyD7Ja5JBjUQW72ktpvXAVD7EWUUsq6EpvI"
//        private const val DEFAULT_RADIUS = 1500
//        // Used for selecting the current place.
//    }
//
//    override fun onCameraIdle() {
//        //镜头停止移动时
//
//    }
//
//    private lateinit var marker: Marker
//
//    /**
//     * 画定位标记图
//     *
//     * @param lat
//     * @param lng
//     */
//    fun drawMarkers(lat: Double, lng: Double) {
//        //清除之前的定位标记图
//        map?.clear()
//        val markerOptions = MarkerOptions()
//            .position(LatLng(lat, lng))
//            .draggable(true)
//        marker = map?.addMarker(markerOptions)!!
////        marker?.showInfoWindow()
//
//    }
//
//    /**
//     *  获取附近信息-
//     * */
//    private fun getNearbyPlaces(
//        keyword: String,
//        lat: Double,
//        lng: Double,
//        radius: Int,
//        nearbyPlace: String
//    ): Response {
//        val googlePlaceUrl =
//            StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?")
//        googlePlaceUrl.append("keyword=$keyword")
//        googlePlaceUrl.append("location=$lat,$lng")
//        googlePlaceUrl.append("&radius=$radius")
//        googlePlaceUrl.append("&type=$nearbyPlace")
//        googlePlaceUrl.append("&key=$MAP_KEY")
//
//        val client = OkHttpClient().newBuilder()
//            .build()
//        val request: Request = Request.Builder()
//            .url(googlePlaceUrl.toString())
//            .method("GET", null)
//            .build()
//        return client.newCall(request).execute()
//    }
//
//}