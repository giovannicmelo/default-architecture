package com.giovannicampos.defaultarchitecture.feature.sample.di

import android.content.Context
import androidx.room.Room
import com.giovannicampos.defaultarchitecture.feature.sample.data.framework.db.SampleDao
import com.giovannicampos.defaultarchitecture.feature.sample.data.framework.db.SampleDatabase
import com.giovannicampos.defaultarchitecture.feature.sample.data.framework.service.SampleService
import com.giovannicampos.defaultarchitecture.feature.sample.data.repository.SampleRepositoryImpl
import com.giovannicampos.defaultarchitecture.feature.sample.data.source.local.SampleLocalDataSource
import com.giovannicampos.defaultarchitecture.feature.sample.data.source.local.SampleLocalDataSourceImpl
import com.giovannicampos.defaultarchitecture.feature.sample.data.source.remote.SampleRemoteDataSource
import com.giovannicampos.defaultarchitecture.feature.sample.data.source.remote.SampleRemoteDataSourceImpl
import com.giovannicampos.defaultarchitecture.feature.sample.domain.repository.SampleRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SampleDataModule {

    @Binds
    abstract fun bindSampleRemoteDataSource(impl: SampleRemoteDataSourceImpl): SampleRemoteDataSource

    @Binds
    abstract fun bindSampleLocalDataSource(impl: SampleLocalDataSourceImpl): SampleLocalDataSource

    @Binds
    @Singleton
    abstract fun bindSampleRepository(impl: SampleRepositoryImpl): SampleRepository

    companion object {

        private const val BASE_URL = "https://example.com/api/"

        private val json = Json { ignoreUnknownKeys = true }

        @Provides
        @Singleton
        fun provideRetrofit(): Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()

        @Provides
        @Singleton
        fun provideSampleService(retrofit: Retrofit): SampleService = retrofit.create()

        @Provides
        @Singleton
        fun provideSampleDatabase(@ApplicationContext context: Context): SampleDatabase =
            Room.databaseBuilder(context, SampleDatabase::class.java, "feature.db").build()

        @Provides
        fun provideSampleDao(database: SampleDatabase): SampleDao = database.sampleDao()
    }
}
