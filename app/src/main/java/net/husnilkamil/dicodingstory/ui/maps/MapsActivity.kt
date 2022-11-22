package net.husnilkamil.dicodingstory.ui.maps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import net.husnilkamil.dicodingstory.R
import net.husnilkamil.dicodingstory.databinding.ActivityMapsBinding
import net.husnilkamil.dicodingstory.data.networks.Response.GetStoryResponse
import net.husnilkamil.dicodingstory.utils.getToken
import net.husnilkamil.dicodingstory.data.networks.NetworkConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val boundsBuilder = LatLngBounds.Builder()

    data class StoryPlace(
        val name: String,
        val lat: Double?,
        val lon: Double?
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 0.0), 5.0f))

        getStories()
    }

    private fun getStories() {
        val service = NetworkConfig.service
        val response = service.getAllStoriesWithLocation(getToken(this), 1)
        response.enqueue(object : Callback<GetStoryResponse?> {

            override fun onResponse(
                call: Call<GetStoryResponse?>,
                response: Response<GetStoryResponse?>
            ) {
                var getStoryResponse: GetStoryResponse? = response.body()
                if (getStoryResponse != null) {
                    val listStory = getStoryResponse.listStory
                    listStory?.forEach { storyItem ->
                        val latLng = LatLng(storyItem?.lat!!, storyItem?.lon!!)
                        Log.d("MAP_DBG", storyItem.lat.toString());
                        Log.d("MAP_DBG", storyItem.lon.toString());
                        Log.d("MAP_DBG", latLng.toString());
                        Log.d("MAP_DBG", storyItem.toString());
                        mMap.addMarker(
                            MarkerOptions()
                                .position(latLng)
                                .title(storyItem.name)
                        )
                        boundsBuilder.include(latLng)
                    }
                    val bounds: LatLngBounds = boundsBuilder.build()
                    mMap.animateCamera(
                        CameraUpdateFactory.newLatLngBounds(
                            bounds,
                            30
                        )
                    )
                }
            }

            override fun onFailure(call: Call<GetStoryResponse?>, t: Throwable) {
                Toast.makeText(this@MapsActivity, "Terjadi kendala teknis", Toast.LENGTH_SHORT)
                    .show()
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.map, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.normal_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                true
            }
            R.id.satellite_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                true
            }
            R.id.terrain_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                true
            }
            R.id.hybrid_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
}