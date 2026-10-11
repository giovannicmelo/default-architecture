package com.giovannicampos.defaultarchitecture.feature.sample.di

import com.giovannicampos.defaultarchitecture.feature.sample.domain.repository.SampleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [SampleDataModule::class])
abstract class FakeSampleDataModule {

    @Binds
    abstract fun bindSampleRepository(fake: FakeSampleRepository): SampleRepository
}
