package com.example.quantpricetracker.presentation

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class CoinGeckoPriceResponse(
    val `quant-network`: PriceData
)

data class PriceData(
    val cad: Double
)

interface CoinGeckoApi {
    @GET("api/v3/simple/price")
    suspend fun getPrice(
        @Query("ids") ids: String = "quant-network",
        @Query("vs_currencies") currencies: String = "cad"
    ): CoinGeckoPriceResponse
    
    companion object {
        private const val BASE_URL = "https://api.coingecko.com/"
        
        fun create(): CoinGeckoApi {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CoinGeckoApi::class.java)
        }
    }
}
