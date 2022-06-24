package com.example.parkingapp.retrofit

import com.example.parkingapp.Parking
import com.example.parkingapp.url
import com.example.parkingapp.compte
import com.example.parkingapp.data_class.Horaire
import com.example.parkingapp.data_class.Parking_Horaire
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface Endpoint {

    @GET("parking")
    suspend fun getAllParkings(): Response<List<Parking>>
    @GET("horaire/{idParking}")
    suspend fun getParkingHoraires(@Path("idParking") idParking:Int): Response<List<Horaire>>
    @GET("horaire/parkings/{jour}")
    suspend fun getHorairesParkings(@Path("jour") jour:String): Response<List<Parking_Horaire>>
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
