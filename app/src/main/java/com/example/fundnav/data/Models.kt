package com.example.fundnav.data

data class FundSearchItem(
    val code: String,
    val name: String,
    val type: String
)

data class FundHolding(
    val stockName: String,
    val ratio: Double,
    val change: Double
)

data class FundNavPoint(
    val date: String,
    val nav: Double,
    val growthRate: Double
)

data class WatchFund(
    val code: String,
    val name: String,
    val estimatedGrowth: Double,
    val reminderEnabled: Boolean
)
