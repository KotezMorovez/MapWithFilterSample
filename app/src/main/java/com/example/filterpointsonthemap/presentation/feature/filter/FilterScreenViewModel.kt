package com.example.filterpointsonthemap.presentation.feature.filter

import androidx.lifecycle.ViewModel
import com.example.filterpointsonthemap.domain.repository_api.MapRepository
import com.example.filterpointsonthemap.presentation.feature.dto.toMapUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class FilterScreenViewModel @Inject constructor(
    private val mapRepository: MapRepository
) : ViewModel() {
    private val displayedServices = mutableSetOf<String>()
    private val _serviceListFlow = MutableStateFlow<List<String>>(listOf())
    val serviceListFlow = _serviceListFlow.asStateFlow()

    fun getServices() {
        val pointsList = mapRepository.getMapPoints().map { it.toMapUI() }
        val serviceSet = mutableSetOf<String>()
        val chosenServices = mapRepository.getServices()

        for (point in pointsList) {
            serviceSet.add(point.service)
        }

        displayedServices.addAll(chosenServices ?: serviceSet)
        _serviceListFlow.value = serviceSet.toList()
    }

    fun updateCheckedServicesList(service: String, isChecked: Boolean) {
        if (isChecked) {
            displayedServices.add(service)
        } else {
            displayedServices.remove(service)
        }
    }

    fun isThisServiceUse(service: String): Boolean {
        return displayedServices.contains(service)
    }

    fun saveServices() {
        mapRepository.saveServices(displayedServices)
    }
}