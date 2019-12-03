package com.example.gps

import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.telephony.cdma.CdmaCellLocation
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var fusedLocationProvideClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback

    val REQUEST_CODE = 1000;

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE -> {
                if (grantResults.size > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                        Toast.makeText(this@MainActivity, "Permition Granted", Toast.LENGTH_SHORT)
                            .show()
                    else
                        Toast.makeText(this@MainActivity, "Permition denied", Toast.LENGTH_SHORT)
                            .show()
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    //melakukan cek permision
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.ACCESS_FINE_LOCATION))
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),REQUEST_CODE)
        else
        {
            buildLocationRequest()
            buildLocationCallback()

            //membuat FusedProvideClient
            fusedLocationProvideClient = LocationServices.getFusedLocationProviderClient(this)

            //set event
            btn_start_update.setOnClickListener(View.OnClickListener {
                if
                        (ActivityCompat.checkSelfPermission(this@MainActivity,android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this@MainActivity,android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(this@MainActivity,arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),REQUEST_CODE)
                    return@OnClickListener
                }
                fusedLocationProvideClient.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper())
            //merubah status tombol
                btn_start_update.isEnabled = !btn_start_update.isEnabled
                btn_stop_update.isEnabled = !btn_stop_update.isEnabled
            });

            btn_stop_update.setOnClickListener(View.OnClickListener {
                if
                        (ActivityCompat.checkSelfPermission(this@MainActivity,android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this@MainActivity,android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(this@MainActivity, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),REQUEST_CODE)
                    return@OnClickListener
                }
                fusedLocationProvideClient.removeLocationUpdates(locationCallback)
                //merubah status tombol
                btn_start_update.isEnabled = !btn_start_update.isEnabled
                btn_stop_update.isEnabled = !btn_stop_update.isEnabled
            });
        }
    }
    private fun buildLocationCallback(){
        locationCallback = object :LocationCallback(){
            //CTRL+O
            override fun onLocationResult(p0: LocationResult?) {
                var location = p0!!.locations.get(p0!!.locations.size-1) //Get Last location
                text_location.text = location.latitude.toString()+"/"+location.longitude.toString()
            }
        }
    }
    private fun buildLocationRequest(){
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.fastestInterval = 3000
        locationRequest.smallestDisplacement = 10f
    }
}
