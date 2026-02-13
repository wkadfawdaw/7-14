package com.example.fundnav.repository

import com.example.fundnav.data.FundHolding
import com.example.fundnav.data.FundNavPoint
import com.example.fundnav.data.FundSearchItem
import com.example.fundnav.data.WatchFund
import com.example.fundnav.network.FundApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FundRepository(private val api: FundApi) {

    suspend fun searchFund(keyword: String): List<FundSearchItem> {
        if (keyword.isBlank()) return emptyList()
        return runCatching {
            api.searchFunds(keyword).map { FundSearchItem(it.code, it.name, it.type) }
        }.getOrElse {
            sampleFundSearch().filter {
                it.code.contains(keyword, ignoreCase = true) ||
                    it.name.contains(keyword, ignoreCase = true)
            }
        }
    }

    suspend fun holdings(code: String): List<FundHolding> {
        return runCatching {
            api.getFundHoldings(code).map { FundHolding(it.stockName, it.ratio, it.change) }
        }.getOrElse { sampleHoldings() }
    }

    suspend fun navHistory(code: String): List<FundNavPoint> {
        return runCatching {
            api.getFundNavHistory(code).map { FundNavPoint(it.date, it.nav, it.growthRate) }
        }.getOrElse { sampleNavHistory() }
    }

    fun watchList(): List<WatchFund> = listOf(
        WatchFund("161725", "招商中证白酒", 1.28, true),
        WatchFund("005827", "易方达蓝筹精选", -0.45, false),
        WatchFund("003095", "中欧医疗健康", 0.92, true)
    )

    companion object {
        fun create(): FundRepository {
            val logger = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            }
            val client = OkHttpClient.Builder().addInterceptor(logger).build()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.example.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return FundRepository(retrofit.create(FundApi::class.java))
        }

        private fun sampleFundSearch() = listOf(
            FundSearchItem("161725", "招商中证白酒指数(LOF)", "指数型"),
            FundSearchItem("110022", "易方达消费行业", "混合型"),
            FundSearchItem("001632", "天弘中证食品饮料", "指数型")
        )

        private fun sampleHoldings() = listOf(
            FundHolding("贵州茅台", 9.3, 0.86),
            FundHolding("五粮液", 7.4, 1.28),
            FundHolding("泸州老窖", 5.1, 0.73),
            FundHolding("山西汾酒", 3.9, 1.15),
            FundHolding("洋河股份", 3.2, -0.24)
        )

        private fun sampleNavHistory() = listOf(
            FundNavPoint("2026-07-10", 1.5623, 0.32),
            FundNavPoint("2026-07-11", 1.5542, -0.52),
            FundNavPoint("2026-07-12", 1.5701, 1.02),
            FundNavPoint("2026-07-13", 1.5760, 0.38),
            FundNavPoint("2026-07-14", 1.5895, 0.86)
        )
    }
}
