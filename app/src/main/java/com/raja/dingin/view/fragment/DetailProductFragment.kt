package com.raja.dingin.view.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cn.pedant.SweetAlert.SweetAlertDialog
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.raja.dingin.R
import com.raja.dingin.connection.API
import com.raja.dingin.databinding.FragmentDetailProductBinding
import com.raja.dingin.model.req.ReqCart
import com.raja.dingin.model.req.ReqDetailProduct
import com.raja.dingin.model.res.ResProduct
import com.raja.dingin.model.res.ResUtama
import com.raja.dingin.utils.ToRupiah
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_detail_product.*
import retrofit2.Response


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [DetailProductFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailProductFragment : Fragment() {

    private var nameProduct: String? = null
    private var harga: Int? = null
    private var token: String? = null
    private var binding: FragmentDetailProductBinding? = null
    private var inv_id: Int? = null
    val imageList = ArrayList<SlideModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailProductBinding.inflate(inflater, container, false)
        val root: View = binding!!.getRoot()

        val prefs =
            requireActivity().baseContext.getSharedPreferences("login", Context.MODE_PRIVATE)
        token = prefs.getString("token", "")

        val bundle = this.arguments
        if (bundle != null) {
            inv_id = bundle.getInt("inv_id")
        }

        val reqDetailProduct = ReqDetailProduct(
            inv_id
        )

        API.buildService().detailProduct(token.toString(), reqDetailProduct)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : Observer<ResProduct> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    Log.d("TAG", "onError: ${e.message}")
                }

                override fun onComplete() {
                    imageSlider.setImageList(imageList, ScaleTypes.FIT)
                    onClick()
                }

                override fun onNext(t: ResProduct) {
                    setview(t)
                }
            })

        // matiin onbackpress
        root.setFocusableInTouchMode(true)
        root.requestFocus()
        root.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (event.action === KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return@OnKeyListener true
                }
            }
            false
        })

        return root
    }

    private fun onClick() {
        binding!!.ivCart.setOnClickListener {
            val qty: Int = binding!!.numberPicker.getValue()
            var total = harga!! * qty

            val resCart = ReqCart(
                harga!!,
                inv_id!!.toInt(),
                nameProduct.toString(),
                number_picker.value,
                total,
            )

            API.buildService().addKeranjang(token.toString(), resCart)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(object : Observer<Response<ResUtama>> {
                    override fun onNext(responseData: Response<ResUtama>) {
                        val statusCode: Int = responseData.code()
                        // here you get your status code
                        if (statusCode == 200) {
                            SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Succeed!")
                                .setConfirmClickListener { sDialog ->
                                    sDialog.dismissWithAnimation()

                                    val fragementIntent = CartFragment()
                                    val frag = activity?.supportFragmentManager?.beginTransaction()
                                    frag!!.replace(R.id.fl_view, fragementIntent)
                                    frag.commit()
                                }
                                .show()
                        } else {

                        }
                    }

                    override fun onError(e: Throwable) {
                        Log.d("TAG", "onError: ${e.message}")
                    }

                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {

                    }
                })
        }
    }

    private fun setview(t: ResProduct) {
        for (i in 0 until t.detailImage.size) {
            imageList.add(SlideModel(t.detailImage.get(i).image))
        }

        tvNameProduct.text = t.name_product
        nameProduct = t.name_product

        tvNamaCabang.text = t.name_cabang
        tvCategori.text = t.kategori
        tvHarga.text = ToRupiah().setConvertRp(t.harga)
        harga = t.harga

        number_picker.setMax(t.sisa_stok)
    }
}