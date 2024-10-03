package com.example.filterpointsonthemap.data.repository

import com.example.filterpointsonthemap.data.dto.toDomain
import com.example.filterpointsonthemap.data.service.MapService
import com.example.filterpointsonthemap.domain.dto.MapPoint
import com.example.filterpointsonthemap.domain.repository_api.MapRepository
import javax.inject.Inject

class MapRepositoryImpl @Inject constructor(
    private val mapService: MapService
) : MapRepository {

    override fun getMapPoints(): List<MapPoint> {
        return mapService.getMapPins().toDomain()
    }

    override fun getServices(): Set<String>? {
        return mapService.getServices()
    }

    override fun saveServices(services: Set<String>) {
        mapService.saveServices(services)
    }
}