package com.raja.dingin

import android.os.Bundle
import android.util.Log
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.raja.dingin.connection.API
import com.raja.dingin.model.prov.*
import com.raja.dingin.model.req.ReqRegister
import com.raja.dingin.model.res.ResUtama
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : AppCompatActivity() {

    private var listDataProvinsi: List<Provinsi> = ArrayList()
    private var listDataKabupaten: List<ResKabupaten> = ArrayList()
    private var listDataKecamatan: List<ResKecamatan> = ArrayList()
    private var listDataKelurahan: List<Kelurahan> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


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
    }

    private fun onChange() {

        spProvinsi.setOnItemClickListener(OnItemClickListener { parent, view, position, id ->
            callKabupaten()
            spKabupaten.text = null
            spKecamatan.text = null
            spKelurahan.text = null
        })

        spKabupaten.setOnItemClickListener(OnItemClickListener { parent, view, position, id ->
            callKecamatan()
            spKecamatan.text = null
            spKelurahan.text = null
        })

        spKecamatan.setOnItemClickListener(OnItemClickListener { parent, view, position, id ->
            callKelurahan()
            spKelurahan.text = null
        })

        btn_register.setOnClickListener {
            if (tvAddress.text.toString().length == 0){
                tvAddress.setError(R.string.can_not_be_empty.toString())
            }
            if (tvEmail.getText().toString().length == 0){
                tvEmail.setError(R.string.can_not_be_empty.toString())
            }
//            if (spKabupaten.getText().toString().trim().length < 1){
//                spKabupaten.setError(R.string.can_not_be_empty.toString())
//                return@setOnClickListener
//            }
//            if (spKecamatan.getText().toString().trim().length < 1){
//                spKecamatan.setError(R.string.can_not_be_empty.toString())
//                return@setOnClickListener
//            }
//            if (spKelurahan.getText().toString().trim().length < 1){
//                spKelurahan.setError(R.string.can_not_be_empty.toString())
//                return@setOnClickListener
//            }
            if (tvName.getText().toString().length == 0){
                tvName.setError(R.string.can_not_be_empty.toString())
            }
            if (tvPassword.getText().toString().length == 0){
                tvPassword.setError(R.string.can_not_be_empty.toString())
            }
            if (tvPhoneNumber.getText().toString().length == 0){
                tvPhoneNumber.setError(R.string.can_not_be_empty.toString())
            }
            if (tvPostalCode.getText().toString().length == 0){
                tvPostalCode.setError(R.string.can_not_be_empty.toString())
            }
//            if (spProvinsi.getText().toString().trim().length < 1){
//                spProvinsi.setError(R.string.can_not_be_empty.toString())
//                return@setOnClickListener
//            }
            if (tvUsername.getText().toString().length == 0){
                tvUsername.setError(R.string.can_not_be_empty.toString())
            }
            else {
                var register = ReqRegister(
                    tvAddress.text.toString(),
                    tvEmail.text.toString(),
                    spKabupaten.text.toString(),
                    spKecamatan.text.toString(),
                    spKelurahan.text.toString(),
                    "-6.551896",
                    "106.807863",
                    tvName.text.toString(),
                    tvPassword.text.toString(),
                    tvPhoneNumber.text.toString(),
                    tvPostalCode.text.toString(),
                    spProvinsi.text.toString(),
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
            //spProvinsi.getSelectedItem().toString()
            spProvinsi.text.toString()
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
            //spProvinsi.getSelectedItem().toString(),
            spProvinsi.text.toString(),
            spKabupaten.text.toString(),
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
            //spProvinsi.getSelectedItem().toString(),
            spProvinsi.text.toString(),
            spKabupaten.text.toString(),
            spKecamatan.text.toString()
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


}