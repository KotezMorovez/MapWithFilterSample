package com.example.filterpointsonthemap.di

import android.content.Context
import com.example.filterpointsonthemap.data.repository.MapRepositoryImpl
import com.example.filterpointsonthemap.data.service.MapService
import com.example.filterpointsonthemap.data.service.MapServiceImpl
import com.example.filterpointsonthemap.domain.repository_api.MapRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface BindsModule {
    @Binds
    @Reusable
    fun bindMapRepository(impl: MapRepositoryImpl): MapRepository
}

@Module
@InstallIn(SingletonComponent::class)
class ProviderModule {
    @Provides
    @Singleton
    fun provideMapService(@ApplicationContext context: Context): MapService {
        return MapServiceImpl(context)
    }
}