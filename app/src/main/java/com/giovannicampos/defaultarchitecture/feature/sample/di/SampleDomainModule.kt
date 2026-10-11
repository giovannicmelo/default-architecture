package com.giovannicampos.defaultarchitecture.feature.sample.di

import com.giovannicampos.defaultarchitecture.feature.sample.domain.repository.SampleRepository
import com.giovannicampos.defaultarchitecture.feature.sample.domain.usecase.GetSamplesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object SampleDomainModule {

    @Provides
    fun provideGetSamplesUseCase(repository: SampleRepository): GetSamplesUseCase =
        GetSamplesUseCase(repository)
}
