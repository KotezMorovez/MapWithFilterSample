package com.example.filterpointsonthemap.presentation.feature.dto

data class MapPointUI(
    val id: Int,
    val service: String,
    val coordinates: Pair<Double, Double>
)