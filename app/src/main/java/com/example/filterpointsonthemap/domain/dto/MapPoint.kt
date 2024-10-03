package com.example.filterpointsonthemap.domain.dto

data class MapPoint(
    val id: Int,
    val service: String,
    val coordinates: Pair<Double, Double>
)
