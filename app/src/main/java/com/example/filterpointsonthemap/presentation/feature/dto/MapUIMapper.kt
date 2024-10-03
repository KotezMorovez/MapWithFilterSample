package com.example.filterpointsonthemap.presentation.feature.dto

import com.example.filterpointsonthemap.domain.dto.MapPoint

fun MapPoint.toMapUI(): MapPointUI {
    return MapPointUI(
        id = this.id,
        service = this.service,
        coordinates = this.coordinates
    )
}