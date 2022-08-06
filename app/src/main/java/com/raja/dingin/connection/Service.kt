package com.raja.dingin.connection

import com.raja.dingin.model.prov.*
import com.raja.dingin.model.req.ReqDetailProduct
import com.raja.dingin.model.req.ReqLogin
import com.raja.dingin.model.req.ReqProduct
import com.raja.dingin.model.req.ReqRegister
import com.raja.dingin.model.res.*
import io.reactivex.Observable

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface Service {

    @POST("Login/login")
    fun getLogin(@Body requestBody: ReqLogin): Observable<ResLogin>

    @POST("Login/getProvinsi")
    fun getProvinsi(): Observable<List<Provinsi>>

    @POST("Login/getKabupaten")
    fun getKabupaten(@Body requestBody: Provinsi): Observable<List<ResKabupaten>>

    @POST("Login/getKecamatan")
    fun getKecamatan(@Body requestBody: ReqKabupaten): Observable<List<ResKecamatan>>

    @POST("Login/getKelurahan")
    fun getKelurahan(@Body requestBody: ReqKecamatan): Observable<List<Kelurahan>>

    @POST("Login/registerCustomer")
    fun registerCustomer(@Body requestBody: ReqRegister): Observable<ResUtama>

    @GET("Api/listBanner")
    fun listBanner(@Header("Authorization") autorization : String): Observable<List<ResBanner>>

    @GET("Api/listKategori")
    fun listKategori(@Header("Authorization") autorization : String): Observable<List<ResCategori>>

    @POST("Api/listProduct")
    fun listProduct(@Header("Authorization") autorization : String, @Body requestBody: ReqProduct): Observable<List<ResProduct>>

    @POST("Api/detailProduct")
    fun detailProduct(@Header("Authorization") autorization : String, @Body requestBody: ReqDetailProduct): Observable<ResProduct>

}