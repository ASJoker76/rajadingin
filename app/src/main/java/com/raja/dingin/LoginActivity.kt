package com.raja.dingin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.raja.dingin.connection.API
import com.raja.dingin.model.req.ReqLogin
import com.raja.dingin.model.res.ResLogin
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.tvPassword
import kotlinx.android.synthetic.main.activity_login.tvRegister
import kotlinx.android.synthetic.main.activity_login.tvUsername


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btn_login.setOnClickListener {
            var login = ReqLogin(
                tvUsername.text.toString(),
                tvPassword.text.toString()
            )
            val compositeDisposable = CompositeDisposable()
            compositeDisposable.add(
                API.buildService().getLogin(login)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ response -> onResponse(response) }, { t -> onFailure(t) })
            )

        }

        tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        tvUsername.setText("bungsu")
        tvPassword.setText("688688")
    }

    private fun onFailure(t: Throwable) {
        Toast.makeText(this, t.message, Toast.LENGTH_SHORT).show()
    }

    private fun onResponse(response: ResLogin) {
        var status = response.status
        if (status == 200) {
            SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Succeed!")
                .setConfirmClickListener { sDialog ->
                    sDialog.dismissWithAnimation()

                    val editor = baseContext.getSharedPreferences("login", Context.MODE_PRIVATE).edit()
                    editor.putString("token", response.token)
                    editor.apply()

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .show()

        } else {
            Toast.makeText(this, status, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        //super.onBackPressed()
    }
}