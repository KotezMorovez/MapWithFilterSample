package com.example.filterpointsonthemap.presentation.feature.map.view_model

import androidx.lifecycle.ViewModel
import com.example.filterpointsonthemap.domain.repository_api.MapRepository
import com.example.filterpointsonthemap.presentation.feature.dto.MapPointUI
import com.example.filterpointsonthemap.presentation.feature.dto.toMapUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val mapRepository: MapRepository
) : ViewModel() {
    private val _pointsListFlow = MutableStateFlow<List<MapPointUI>?>(null)
    val pointsListFlow = _pointsListFlow.asStateFlow()

    private var servicesSet: MutableSet<String>? = null

    fun getPoints() {
        val pointsList = mapRepository.getMapPoints().map { it.toMapUI() }
        getServices()

        if (servicesSet != null) {
            val filteredList = pointsList.filter {
                servicesSet?.contains(it.service) ?: true
            }
            _pointsListFlow.value = filteredList
            return
        }

        _pointsListFlow.value = pointsList
    }

    private fun getServices() {
        mapRepository.getServices()?.let {
            servicesSet = mutableSetOf()
            servicesSet?.addAll(it)
        }
    }
}