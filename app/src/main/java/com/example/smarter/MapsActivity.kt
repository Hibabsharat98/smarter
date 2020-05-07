package com.example.smarter

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps.*
import java.io.IOException
import java.lang.Exception
import android.location.Location
import android.os.Parcel
import android.os.Parcelable
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import kotlinx.android.synthetic.main.activity_drivemode.*


class MapsActivity() : AppCompatActivity(), OnMapReadyCallback, Parcelable {

    private lateinit var mMap: GoogleMap
    var lat :Double=0.0
    var long:Double=0.0
      lateinit var  LatLongg:LatLng
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        search_bt.setOnClickListener {
            val locatioon=  editText.text.toString()
            var addreslist  : List<Address>?=null
            val option = MarkerOptions()





            if (locatioon!="")

            { val gocoder = Geocoder(this)
                try {
                    addreslist=gocoder.getFromLocationName(locatioon,5)
                }catch (e:IOException){e.printStackTrace()}

                for (i in addreslist!!.indices)
                {
                    val adress = addreslist[i]
                    val latlang =LatLng(adress.latitude,adress.longitude)
                    option.position(latlang)
                    option.title("jordan")
                    mMap!!.addMarker(option.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin7)))
                    mMap!!.animateCamera( CameraUpdateFactory.newLatLngZoom(latlang, 12f))
                }


            }
        }


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        checkpermetion()

        SAVE.setOnClickListener {
            var intent=Intent(this,note_cerate::class.java)
            intent.putExtra("long" , long)
            intent.putExtra("lat" , lat)
            //intent.putExtra("long" , LatLongg.longitude)
            //intent.putExtra("lat" , LatLongg.latitude)
        //    if (locationK.latitude !=0.0) {
            //    check
            startActivity(intent)
        //    }
        }




    }
    val accesslocation = 123
    fun checkpermetion(){
        if(Build.VERSION.SDK_INT>=23){
            if (ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(arrayOf( android.Manifest.permission.ACCESS_FINE_LOCATION),accesslocation)
                return}

        }

        getuserlocation ()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        when(requestCode)
        {
            accesslocation->{if (grantResults[0]== PackageManager.PERMISSION_GRANTED){getuserlocation()}
            else{ Toast.makeText(this,"location access is deny", Toast.LENGTH_LONG).show()}
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    fun getuserlocation (){
        Toast.makeText(this,"location access now ", Toast.LENGTH_LONG).show()

        val mylocation = Mylocationlicenare()
        val locationmaneger  = getSystemService(Context.LOCATION_SERVICE)  as LocationManager
        locationmaneger.requestLocationUpdates(LocationManager.GPS_PROVIDER,3,3f,mylocation)
        val mythread= myThread()
        mythread.start()
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        setMapLongClick(mMap)
        // Add a marker in Sydney and move the camera

    }

    lateinit var location : Location

    constructor(parcel: Parcel) : this() {
        location = parcel.readParcelable(Location::class.java.classLoader)!!
    }

    inner class Mylocationlicenare : LocationListener  {
        constructor() {
            location = Location("me")
            location!!.longitude = 0.0
            location!!.latitude = 0.0
        }

        override fun onLocationChanged(locp: Location ) {
            location = locp
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

        }

        override fun onProviderEnabled(provider: String?) {

        }

        override fun onProviderDisabled(provider: String?) {

        }

    }




    inner class myThread:Thread  {
        constructor():super(){}

        override fun run() {
            var x =0
            while (x<5) {
                try {
                    runOnUiThread {
                        mMap.clear()
                        val MyLocation  = LatLng(location!!.latitude, location!!.longitude)
                        mMap.addMarker(
                            MarkerOptions().position(MyLocation).title
                                ("Me ").icon(BitmapDescriptorFactory.fromResource(R.drawable.pin7))
                        )
                        mMap.uiSettings.isZoomControlsEnabled
                        mMap.mapType=GoogleMap.MAP_TYPE_HYBRID
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(MyLocation, 16f))



                    }
                    x++
                    Thread.sleep(1000)
                }catch (ex:Exception){}

            }


        }


    }

    private fun setMapLongClick(map: GoogleMap) {
        mMap.setOnMapLongClickListener { latLng ->

          //  LatLongg=latLng



            mMap.addMarker(
                MarkerOptions().position(latLng).title
                    ("Me ").icon(BitmapDescriptorFactory.fromResource(R.drawable.pin7))

            )

            Toast.makeText(this,"Lat:"+latLng.latitude+","+"Lon:"+latLng.longitude, Toast.LENGTH_LONG).show()
            lat=latLng.latitude
            long=latLng.longitude



        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(location, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MapsActivity> {
        override fun createFromParcel(parcel: Parcel): MapsActivity {
            return MapsActivity(parcel)
        }

        override fun newArray(size: Int): Array<MapsActivity?> {
            return arrayOfNulls(size)
        }
    }



}









