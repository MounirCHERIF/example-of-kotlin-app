package com.example.parkingapp.retrofit

import com.example.parkingapp.Parking
import com.example.parkingapp.url
import com.example.parkingapp.compte
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface Endpoint {

    @GET("parkings/getall")
    suspend fun getAllParkings(): Response<List<Parking>>
    @GET("parkings/getbyname/{name}")
    suspend fun getParkingByName(@Path("name") name: String): Response<List<Parking>>
    @POST("account/create_account")
    suspend fun createAccount(@Body newAccount: compte):Response<String>


    companion object {
        @Volatile
        var endpoint: Endpoint? = null
        fun createEndpoint(): Endpoint {
            if(endpoint ==null) {
                synchronized(this) {
                    endpoint = Retrofit.Builder().baseUrl(url)
                        .addConverterFactory(GsonConverterFactory.create()).build()
                        .create(Endpoint::class.java)
                }
            }
            return endpoint!!

        }


    }

}