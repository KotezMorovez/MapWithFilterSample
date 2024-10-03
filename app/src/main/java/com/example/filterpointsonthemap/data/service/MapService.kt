package com.example.filterpointsonthemap.data.service

import android.content.Context
import com.example.filterpointsonthemap.data.dto.MapPinsEntity
import com.google.gson.Gson
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject

interface MapService {
    fun getMapPins(): MapPinsEntity
    fun getServices(): Set<String>?
    fun saveServices(services: Set<String>)
}

class MapServiceImpl @Inject constructor(
    private val context: Context
) : MapService {
    override fun getMapPins(): MapPinsEntity {
        val file = getJsonFromAssets(context)
        val gson = Gson()

        return gson.fromJson(file, MapPinsEntity::class.java)
    }

    override fun getServices(): Set<String>? {
        return servicesStorage
    }

    override fun saveServices(services: Set<String>) {
        servicesStorage = mutableSetOf()
        servicesStorage?.addAll(services)

    }

    private fun getJsonFromAssets(context: Context): String {
        var json = ""

        try {
            val stream: InputStream = context.assets.open("pins.json")
            val size = stream.available()
            val buffer = ByteArray(size)

            stream.read(buffer)
            stream.close()

            json = String(buffer, charset("UTF-8"))
        } catch (e: IOException) {
            e.printStackTrace()
            return json
        }

        return json
    }

    companion object {
        private var servicesStorage: MutableSet<String>? = null
    }
}