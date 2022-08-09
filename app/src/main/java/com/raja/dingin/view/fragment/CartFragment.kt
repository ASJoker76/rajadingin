package com.raja.dingin.view.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
//import com.raja.dingin.adapter.AdapterCart
import com.raja.dingin.adapter.AdapterCart
import com.raja.dingin.adapter.RecyclerViewCartClickListener
import com.raja.dingin.connection.API
import com.raja.dingin.databinding.FragmentCartBinding
import com.raja.dingin.model.req.ReqProduct
import com.raja.dingin.model.res.ResCart
import com.raja.dingin.utils.ToRupiah
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response


class CartFragment : Fragment(), RecyclerViewCartClickListener {

    private lateinit var binding: FragmentCartBinding
    private var token: String? = null
    private var listDataCart: MutableList<ResCart> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(inflater, container, false)
        val root: View = binding!!.getRoot()

        val prefs =
            requireActivity().baseContext.getSharedPreferences("login", Context.MODE_PRIVATE)
        token = prefs.getString("token", "")

        val reqProduct = ReqProduct(
            10,
            0,
            ""
        )

        API.buildService().listKeranjang(token.toString(), reqProduct)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribeWith(object : Observer<Response<List<ResCart>>> {
                override fun onNext(responseData: Response<List<ResCart>>) {
                    listDataCart.clear()
                    val statusCode: Int = responseData.code()
                    // here you get your status code
                    if (statusCode == 200) {
                        listDataCart = responseData.body() as MutableList<ResCart>
                        loadrecylerviewProduct(listDataCart)
                        cekkondisi()
                    } else if (statusCode == 204) {
                        //cartAdapter.setView(listDataCart)
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

        return root
    }

    private fun loadrecylerviewProduct(resProduct: List<ResCart>) {
        val recyclerView = binding!!.rvCart

        val cartAdapter = AdapterCart(resProduct as ArrayList<ResCart>)
        // set click listener
        cartAdapter.listener = this

        recyclerView.apply {
            this.adapter = cartAdapter
            this.layoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        }

        //cartAdapter.setView(resProduct)
    }

    override fun onItemClicked(total: Long) {
        binding.tvTotal.setText(ToRupiah().setConvertRp(total.toInt()))
        cekkondisi()
    }

    private fun cekkondisi() {
        if(binding.tvTotal.text!!.isEmpty()){
            binding.btnPay.isEnabled = false
        }
        else{
            binding.btnPay.isEnabled = true
        }
    }
}