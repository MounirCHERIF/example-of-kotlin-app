package com.example.parkingapp.retrofit

import com.example.parkingapp.Parking
import com.example.parkingapp.url
import com.example.parkingapp.compte
import com.example.parkingapp.data_class.Reservation
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface Endpoint {

    @GET("parking")
    suspend fun getAllParkings(): Response<List<Parking>>
    @POST("compte")
    suspend fun createAccount(@Body newAccount: compte):Response<compte>

    @GET("/compte")
    suspend fun getComptes(): Response<List<compte>>

    @GET("/compte/{id}?type=id")
    suspend fun getCompteById(@Path("id") id: String): Response<compte>

    @GET("/compte/{email}?type=email")
    suspend fun getCompteByEmail(@Path("email") email: String): Response<compte>

    @PUT("/compte/{id}")
    suspend fun updateCompte(@Path("id") id: String): Response<compte>

    @DELETE("/compte/{id}")
    suspend fun deleteCompte(@Path("id") id: String): Response<compte>


    @GET("/reservation")
    suspend fun getReservations(): Response<List<Reservation>>

    @POST("/reservation")
    suspend fun createReservation(@Body reservation: Reservation) : Response<Reservation>

    @GET("/reservation/{id}?type=id")
    suspend fun getReservationById(@Path("id") id: String): Response<Reservation>

    @GET("/reservation/{id}?type=parking")
    suspend fun getReservationsByParking(@Path("id") id: String): Response<List<Reservation>>

    @PUT("/reservation/{id}")
    suspend fun upateReservation(@Path("id") id: String): Response<Reservation>

    @DELETE("/reservation/{id}")
    suspend fun deleteReservation(@Path("id") id: String): Response<Reservation>


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
