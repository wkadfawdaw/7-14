package com.example.fundnav.network

import retrofit2.http.GET
import retrofit2.http.Query

interface FundApi {
    @GET("fund/search")
    suspend fun searchFunds(@Query("keyword") keyword: String): List<FundSearchDto>

    @GET("fund/holdings")
    suspend fun getFundHoldings(@Query("code") code: String): List<FundHoldingDto>

    @GET("fund/nav-history")
    suspend fun getFundNavHistory(@Query("code") code: String): List<FundNavDto>
}

data class FundSearchDto(
    val code: String,
    val name: String,
    val type: String
)

data class FundHoldingDto(
    val stockName: String,
    val ratio: Double,
    val change: Double
)

data class FundNavDto(
    val date: String,
    val nav: Double,
    val growthRate: Double
)
