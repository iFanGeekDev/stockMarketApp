package com.example.stockmarketapp.domain.model

data class CompanyInfo(
    val name: String,
    val symbol: String,
    val description: String,
    val country: String,
    val industry: String
)