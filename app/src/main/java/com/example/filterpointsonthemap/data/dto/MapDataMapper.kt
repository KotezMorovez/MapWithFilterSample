package com.example.filterpointsonthemap.data.dto

import com.example.filterpointsonthemap.domain.dto.MapPoint

fun MapPinsEntity.toDomain(): List<MapPoint> {
    return this.pins.map { it.toDomain() }
}

fun MapPointEntity.toDomain(): MapPoint {
    return MapPoint(
        id = this.id,
        service = this.service,
        coordinates = Pair (this.coordinates.lat, this.coordinates.lng),
    )
}