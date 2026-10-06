package com.giovannicampos.defaultarchitecture.feature.di

import android.content.Context
import androidx.room.Room
import com.giovannicampos.defaultarchitecture.feature.data.framework.db.FeatureDao
import com.giovannicampos.defaultarchitecture.feature.data.framework.db.FeatureDatabase
import com.giovannicampos.defaultarchitecture.feature.data.framework.service.FeatureService
import com.giovannicampos.defaultarchitecture.feature.data.repository.FeatureRepositoryImpl
import com.giovannicampos.defaultarchitecture.feature.data.source.local.FeatureLocalDataSource
import com.giovannicampos.defaultarchitecture.feature.data.source.local.FeatureLocalDataSourceImpl
import com.giovannicampos.defaultarchitecture.feature.data.source.remote.FeatureRemoteDataSource
import com.giovannicampos.defaultarchitecture.feature.data.source.remote.FeatureRemoteDataSourceImpl
import com.giovannicampos.defaultarchitecture.feature.domain.repository.FeatureRepository
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
abstract class FeatureDataModule {

    @Binds
    abstract fun bindFeatureRemoteDataSource(impl: FeatureRemoteDataSourceImpl): FeatureRemoteDataSource

    @Binds
    abstract fun bindFeatureLocalDataSource(impl: FeatureLocalDataSourceImpl): FeatureLocalDataSource

    @Binds
    @Singleton
    abstract fun bindFeatureRepository(impl: FeatureRepositoryImpl): FeatureRepository

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
        fun provideFeatureService(retrofit: Retrofit): FeatureService = retrofit.create()

        @Provides
        @Singleton
        fun provideFeatureDatabase(@ApplicationContext context: Context): FeatureDatabase =
            Room.databaseBuilder(context, FeatureDatabase::class.java, "feature.db").build()

        @Provides
        fun provideFeatureDao(database: FeatureDatabase): FeatureDao = database.featureDao()
    }
}
