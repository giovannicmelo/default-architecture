package com.giovannicampos.defaultarchitecture.feature.di

import com.giovannicampos.defaultarchitecture.feature.domain.repository.FeatureRepository
import com.giovannicampos.defaultarchitecture.feature.domain.usecase.GetFeaturesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object FeatureDomainModule {

    @Provides
    fun provideGetFeaturesUseCase(repository: FeatureRepository): GetFeaturesUseCase =
        GetFeaturesUseCase(repository)
}
