package com.giovannicampos.defaultarchitecture.feature.di

import com.giovannicampos.defaultarchitecture.feature.domain.repository.FeatureRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [FeatureDataModule::class])
abstract class FakeFeatureDataModule {

    @Binds
    abstract fun bindFeatureRepository(fake: FakeFeatureRepository): FeatureRepository
}
