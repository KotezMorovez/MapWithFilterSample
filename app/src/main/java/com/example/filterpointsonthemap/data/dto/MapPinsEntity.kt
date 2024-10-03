package com.example.filterpointsonthemap.data.dto

data class MapPinsEntity(
    val services: List<String>,
    val pins: List<MapPointEntity>
)