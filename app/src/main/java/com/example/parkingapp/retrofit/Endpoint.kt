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

interface Endpoint {

    @GET("parking")
    suspend fun getAllParkings(): Response<List<Parking>>
    @POST("compte")
    suspend fun createAccount(@Body newAccount: compte):Response<compte>


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
