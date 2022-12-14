package com.raja.dingin

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.get
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.raja.dingin.connection.API
import com.raja.dingin.model.prov.*
import com.raja.dingin.model.req.ReqRegister
import com.raja.dingin.model.res.ResUtama
import com.raja.dingin.utils.LocationFormatter
import id.rizmaulana.sheenvalidator.lib.SheenValidator
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : AppCompatActivity(), LocationFormatter {

    private lateinit var sheenValidator: SheenValidator

    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var lastLocation: Location? = null
    private var latitudeLabel: String? = null
    private var longitudeLabel: String? = null

    private var listDataProvinsi: List<Provinsi> = ArrayList()
    private var listDataKabupaten: List<ResKabupaten> = ArrayList()
    private var listDataKecamatan: List<ResKecamatan> = ArrayList()
    private var listDataKelurahan: List<Kelurahan> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        API.buildService().getProvinsi()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<Provinsi?>?> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    onFailure(e)
                }

                override fun onComplete() {
                    callKabupaten()
                }

                override fun onNext(t: List<Provinsi?>) {
                    listDataProvinsi = t as List<Provinsi>

                    onResponseProvinsi(listDataProvinsi)
                }
            })

        onChange()

        sheenValidator = SheenValidator(this)
        sheenValidator.setOnValidatorListener {
            if(tvPasswordConfirm.text.toString().equals(tvPassword.text.toString())){
                var register = ReqRegister(
                    tvAddress.text.toString(),
                    tvEmail.text.toString(),
                    tvKabupaten.text.toString(),
                    tvKecamatan.text.toString(),
                    tvKelurahan.text.toString(),
                    latitudeLabel.toString(),
                    longitudeLabel.toString(),
                    tvName.text.toString(),
                    tvPassword.text.toString(),
                    tvPhoneNumber.text.toString(),
                    tvPostalCode.text.toString(),
                    tvProvinsi.text.toString(),
                    tvUsername.text.toString()
                )

                API.buildService().registerCustomer(register)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<ResUtama> {
                        override fun onSubscribe(d: Disposable) {

                        }

                        override fun onError(e: Throwable) {
                            onFailure(e)
                        }

                        override fun onComplete() {
                            callKabupaten()
                        }

                        override fun onNext(t: ResUtama) {
                            onSuccess(t)
                        }
                    })
            }
            else{
                tvPassword.error = "Password Not Macth"
                tvPasswordConfirm.error = "Password Not Macth"
            }
        }
        sheenValidator.registerAsRequired(tvEmail)
        sheenValidator.registerAsEmail(tvEmail)
        sheenValidator.registerAsRequired(tvName)
        sheenValidator.registerAsRequired(tvUsername)
        sheenValidator.registerAsPhone(tvPhoneNumber)
        sheenValidator.registerAsRequired(tvAddress)
        sheenValidator.registerAsRequired(tvPostalCode)
        sheenValidator.registerAsRequired(tvPhoneNumber)
        sheenValidator.registerAsRequired(tvPassword)
        sheenValidator.registerAsRequired(tvPasswordConfirm)
    }

    private fun onChange() {

        tvProvinsi.setOnClickListener{
            spProvinsi.performClick()
        }

        spProvinsi.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                tvProvinsi.setText(spProvinsi.selectedItem.toString())
                callKabupaten()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // your code here
            }
        })

        tvKabupaten.setOnClickListener{
            spKabupaten.performClick()
        }

        spKabupaten.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                tvKabupaten.setText(spKabupaten.selectedItem.toString())
                callKecamatan()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // your code here
            }
        })

        tvKecamatan.setOnClickListener{
            spKecamatan.performClick()
        }

        spKecamatan.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                tvKecamatan.setText(spKecamatan.selectedItem.toString())
                callKelurahan()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // your code here
            }
        })

        tvKelurahan.setOnClickListener{
            spKelurahan.performClick()
        }

        spKelurahan.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                tvKelurahan.setText(spKelurahan.selectedItem.toString())
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // your code here
            }
        })

        btn_register.setOnClickListener {
            sheenValidator.validate()
        }
    }

    private fun onSuccess(resUtama: ResUtama) {
        if(resUtama.msg.equals("success")){
            SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Succeed!")
                .show()
        }
        else{
            SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText(resUtama.msg.toString())
                .show()
        }
    }

    private fun onFailure(t: Throwable) {
        //Toast.makeText(this, t.message, Toast.LENGTH_SHORT).show()
        SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
            .setTitleText("Oops...")
            .setContentText(t.message)
            .show()
    }

    private fun callKabupaten() {
        var provinsi = Provinsi(
            spProvinsi.getSelectedItem().toString()
//            spProvinsi.text.toString()
        )

        API.buildService().getKabupaten(provinsi)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : Observer<List<ResKabupaten?>?> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    Log.d("TAG", "onError: ${e.message}")
                }

                override fun onComplete() {
                    callKecamatan()
                }

                override fun onNext(t: List<ResKabupaten?>) {
                    listDataKabupaten = t as List<ResKabupaten>

                    onResponseKabupaten(listDataKabupaten)
                }
            })
    }

    private fun callKecamatan() {
        var kabupaten = ReqKabupaten(
            spProvinsi.getSelectedItem().toString(),
            spKabupaten.getSelectedItem().toString(),
        )

        API.buildService().getKecamatan(kabupaten)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : Observer<List<ResKecamatan?>?> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    Log.d("TAG", "onError: ${e.message}")
                }

                override fun onComplete() {
                    callKelurahan()
                }

                override fun onNext(t: List<ResKecamatan?>) {
                    listDataKecamatan = t as List<ResKecamatan>

                    onResponseKecamatan(listDataKecamatan)
                }
            })
    }

    private fun callKelurahan() {
        var kecamatan = ReqKecamatan(
            spProvinsi.getSelectedItem().toString(),
            spKabupaten.getSelectedItem().toString(),
            spKecamatan.getSelectedItem().toString()
        )

        API.buildService().getKelurahan(kecamatan)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : Observer<List<Kelurahan?>?> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    Log.d("TAG", "onError: ${e.message}")
                }

                override fun onComplete() {

                }

                override fun onNext(t: List<Kelurahan?>) {
                    listDataKelurahan = t as List<Kelurahan>

                    onResponseKelurahan(listDataKelurahan)
                }
            })
    }


    private fun onResponseProvinsi(response: List<Provinsi>) {
        listDataProvinsi = response

        val list: MutableList<String> = ArrayList()
        for (loopProvinsi in listDataProvinsi) {
            list.add(loopProvinsi.provinsi)
        }
        // Create an ArrayAdapter using a simple spinner layout and languages array
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, list)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spProvinsi!!.setAdapter(aa)
    }

    private fun onResponseKabupaten(response: List<ResKabupaten>) {
        listDataKabupaten = response

        val list: MutableList<String> = ArrayList()
        for (loopProvinsi in listDataKabupaten) {
            list.add(loopProvinsi.kab_kota)
        }
        // Create an ArrayAdapter using a simple spinner layout and languages array
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, list)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spKabupaten!!.setAdapter(aa)
    }

    private fun onResponseKecamatan(response: List<ResKecamatan>) {
        listDataKecamatan = response

        val list: MutableList<String> = ArrayList()
        for (loopProvinsi in listDataKecamatan) {
            list.add(loopProvinsi.kecamatan)
        }
        // Create an ArrayAdapter using a simple spinner layout and languages array
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, list)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spKecamatan!!.setAdapter(aa)
    }

    private fun onResponseKelurahan(response: List<Kelurahan>) {
        listDataKelurahan = response

        val list: MutableList<String> = ArrayList()
        for (loopProvinsi in listDataKelurahan) {
            list.add(loopProvinsi.kelurahan)
        }
        // Create an ArrayAdapter using a simple spinner layout and languages array
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, list)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spKelurahan!!.setAdapter(aa)
    }


    public override fun onStart() {
        super.onStart()
        if (!checkPermissions()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions()
            }
        }
        else {
            getLastLocation()
        }
    }
    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        fusedLocationClient?.lastLocation!!.addOnCompleteListener(this) { task ->
            if (task.isSuccessful && task.result != null) {
                lastLocation = task.result
                latitudeLabel = (lastLocation)!!.latitude.toString()
                longitudeLabel = (lastLocation)!!.longitude.toString()

                Log.d(TAG, "latitude: ${(lastLocation)!!.latitude}")
                Log.d(TAG, "longitude: ${(lastLocation)!!.longitude}")
            }
            else {
                Log.w(TAG, "getLastLocation:exception", task.exception)
                showMessage("No location detected. Make sure location is enabled on the device.")
            }
        }
    }

    private fun showMessage(string: String) {
        Toast.makeText(this@RegisterActivity, string, Toast.LENGTH_LONG).show()
    }
    private fun showSnackbar(
        mainTextStringId: String, actionStringId: String,
        listener: View.OnClickListener
    ) {
        Toast.makeText(this@RegisterActivity, mainTextStringId, Toast.LENGTH_LONG).show()
    }
    private fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return permissionState == PackageManager.PERMISSION_GRANTED
    }
    private fun startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(
            this@RegisterActivity,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            REQUEST_PERMISSIONS_REQUEST_CODE
        )
    }
    private fun requestPermissions() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.")
            showSnackbar("Location permission is needed for core functionality", "Okay",
                View.OnClickListener {
                    startLocationPermissionRequest()
                })
        }
        else {
            Log.i(TAG, "Requesting permission")
            startLocationPermissionRequest()
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.i(TAG, "onRequestPermissionResult")
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            when {
                grantResults.isEmpty() -> {
                    // If user interaction was interrupted, the permission request is cancelled and you
                    // receive empty arrays.
                    Log.i(TAG, "User interaction was cancelled.")
                }
                grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                    // Permission granted.
                    getLastLocation()
                }
                else -> {
                    showSnackbar("Permission was denied", "Settings",
                        View.OnClickListener {
                            // Build intent that displays the App settings screen.
                            val intent = Intent()
                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            val uri = Uri.fromParts(
                                "package",
                                Build.DISPLAY, null
                            )
                            intent.data = uri
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }
                    )
                }
            }
        }
    }
    companion object {
        private val TAG = "LocationProvider"
        private val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    }

    override fun onBackPressed() {
        //super.onBackPressed()
    }
}