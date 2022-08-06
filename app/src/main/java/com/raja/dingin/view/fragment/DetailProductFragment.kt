package com.raja.dingin.view.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.raja.dingin.connection.API
import com.raja.dingin.databinding.FragmentDetailProductBinding
import com.raja.dingin.model.req.ReqDetailProduct
import com.raja.dingin.model.res.ResProduct
import com.raja.dingin.utils.ToRupiah
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_detail_product.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.imageSlider

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [DetailProductFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailProductFragment : Fragment() {

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

        val prefs = requireActivity().baseContext.getSharedPreferences("login", Context.MODE_PRIVATE)
        token = prefs.getString("token", "")

        val bundle = this.arguments
        if (bundle != null) {
            inv_id = bundle.getInt("inv_id")
        }

        val reqDetailProduct = ReqDetailProduct(
           inv_id
        )

        API.buildService().detailProduct(token.toString(),reqDetailProduct)
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
                }

                override fun onNext(t: ResProduct) {
                    setview(t)
                }
            })

        return root
    }

    private fun setview(t: ResProduct) {
        for (i in 0 until t.detailImage.size) {
            imageList.add(SlideModel(t.detailImage.get(i).image))
        }

        tvNameProduct.text = t.name_product
        tvNamaCabang.text = t.name_cabang
        tvCategori.text = t.kategori
        tvHarga.text = ToRupiah().setConvertRp(t.harga)

        number_picker.setMax(t.sisa_stok)
    }
}