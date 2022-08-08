package com.raja.dingin.view.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.raja.dingin.R
import com.raja.dingin.adapter.AdapterCategori
import com.raja.dingin.adapter.AdapterProduct
import com.raja.dingin.adapter.RecyclerViewHomeClickListener
import com.raja.dingin.connection.API
import com.raja.dingin.databinding.FragmentHomeBinding
import com.raja.dingin.model.req.ReqProduct
import com.raja.dingin.model.res.ResBanner
import com.raja.dingin.model.res.ResCategori
import com.raja.dingin.model.res.ResProduct
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Response


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment(), RecyclerViewHomeClickListener {

    private var binding: FragmentHomeBinding? = null

    private var param1: String? = null
    private var param2: String? = null

    private var listData: List<ResBanner> = ArrayList()
    private var listDataCategori: List<ResCategori> = ArrayList()
    private var listDataProduct: MutableList<ResProduct> = mutableListOf()
    private var token: String? = null

    val productAdapter = AdapterProduct()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding!!.getRoot()

        val prefs = requireActivity().baseContext.getSharedPreferences("login", Context.MODE_PRIVATE)
        token = prefs.getString("token", "")

        val imageList = ArrayList<SlideModel>()

        API.buildService().listBanner(token.toString())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : Observer<List<ResBanner?>?> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    Log.d("TAG", "onError: ${e.message}")
                }

                override fun onComplete() {
                    imageSlider.setImageList(imageList, ScaleTypes.FIT)

                    loadcategori()
                }

                override fun onNext(t: List<ResBanner?>) {
                    listData = t as List<ResBanner>

                    for (i in 0 until listData.size) {
                        imageList.add(SlideModel(listData.get(i).image,listData.get(i).name_banner))
                    }

                }
            })

        return root
    }

    private fun loadcategori() {
        API.buildService().listKategori(token.toString())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : Observer<List<ResCategori?>?> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    Log.d("TAG", "onError: ${e.message}")
                }

                override fun onComplete() {
                    loadproduct("")
                }

                override fun onNext(t: List<ResCategori?>) {
                    listDataCategori = t as List<ResCategori>
                    loadrecylerviewCategori(listDataCategori)
                }
            })
    }

    private fun loadproduct(param: String) {
        val reqProduct = ReqProduct(
            10,
            0,
            param
        )
        API.buildService().listProduct(token.toString(),reqProduct)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribeWith(object : Observer<Response<List<ResProduct>>> {
                override fun onNext(responseData: Response<List<ResProduct>>) {
                    listDataProduct.clear()
                    val statusCode: Int = responseData.code()
                    // here you get your status code
                    if (statusCode==200){
                        listDataProduct = responseData.body() as MutableList<ResProduct>
                        loadrecylerviewProduct(listDataProduct)
                    }
                    else if (statusCode==204){
                        productAdapter.setView(listDataProduct)
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

    private fun loadrecylerviewCategori(resCategori: List<ResCategori>) {
        val categoriAdapter = AdapterCategori(resCategori)
        val recyclerView = binding!!.rvCategori

        // set click listener
        categoriAdapter.listener = this

        recyclerView.apply {
            this.adapter = categoriAdapter
            this.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun loadrecylerviewProduct(resProduct: List<ResProduct>) {
        val recyclerView = binding!!.rvProduct

        // set click listener
        productAdapter.listener = this

        recyclerView.apply {
            this.adapter = productAdapter
            this.layoutManager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
        }

        productAdapter.setView(resProduct)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onItemClicked(view: View, resCategori: ResCategori) {
        if (resCategori.kategori_id==0){
            loadproduct("")
        }
        else{
            loadproduct(resCategori.kategori_id.toString())
        }
    }

    override fun onItemClicked(view: View, resProduct: ResProduct) {
        val bundle = Bundle()
        bundle.putInt("inv_id", resProduct.inv_id)
        val fragementIntent = DetailProductFragment()
        val frag = activity?.supportFragmentManager?.beginTransaction()
        frag!!.replace(R.id.fl_view,fragementIntent)
        fragementIntent.setArguments(bundle)
        frag.commit()
    }
}