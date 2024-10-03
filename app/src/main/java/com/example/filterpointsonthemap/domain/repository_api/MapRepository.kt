package com.example.filterpointsonthemap.domain.repository_api

import com.example.filterpointsonthemap.domain.dto.MapPoint

interface MapRepository {
    fun getMapPoints(): List<MapPoint>
    fun getServices(): Set<String>?
    fun saveServices(services: Set<String>)
}