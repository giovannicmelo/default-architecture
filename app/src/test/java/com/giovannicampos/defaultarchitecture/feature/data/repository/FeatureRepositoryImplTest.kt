package com.giovannicampos.defaultarchitecture.feature.data.repository

import com.giovannicampos.defaultarchitecture.feature.data.model.FeatureEntity
import com.giovannicampos.defaultarchitecture.feature.data.source.local.FeatureLocalDataSource
import com.giovannicampos.defaultarchitecture.feature.data.source.remote.FeatureRemoteDataSourceImpl
import com.giovannicampos.defaultarchitecture.feature.domain.model.Feature
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.create

class FeatureRepositoryImplTest {

    private val server = MockWebServer()
    private val json = Json { ignoreUnknownKeys = true }
    private val localDataSource = mockk<FeatureLocalDataSource>(relaxUnitFun = true)
    private lateinit var repository: FeatureRepositoryImpl

    @Before
    fun setUp() {
        server.start()
        val retrofit = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
        repository = FeatureRepositoryImpl(
            remoteDataSource = FeatureRemoteDataSourceImpl(retrofit.create()),
            localDataSource = localDataSource,
        )
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `given api returns features, when getFeatures, then saves them locally and returns local features`() = runTest {
        // Given
        server.enqueue(MockResponse().setResponseCode(200).setBody("""[{"id":"1","name":"Primeira"}]"""))
        coEvery { localDataSource.getFeatures() } returns listOf(FeatureEntity(id = "1", name = "Primeira"))

        // When
        val result = repository.getFeatures()

        // Then
        assertEquals("/features", server.takeRequest().path)
        coVerify { localDataSource.saveFeatures(listOf(FeatureEntity(id = "1", name = "Primeira"))) }
        assertEquals(listOf(Feature(id = "1", name = "Primeira")), result)
    }

    @Test
    fun `given network is unavailable, when getFeatures, then returns cached features without saving`() = runTest {
        // Given
        server.enqueue(MockResponse().setSocketPolicy(SocketPolicy.DISCONNECT_AT_START))
        coEvery { localDataSource.getFeatures() } returns listOf(FeatureEntity(id = "1", name = "Em cache"))

        // When
        val result = repository.getFeatures()

        // Then
        coVerify(exactly = 0) { localDataSource.saveFeatures(any()) }
        assertEquals(listOf(Feature(id = "1", name = "Em cache")), result)
    }

    @Test
    fun `given api returns server error, when getFeatures, then throws HttpException`() = runTest {
        // Given
        server.enqueue(MockResponse().setResponseCode(500))

        // When
        val result = runCatching { repository.getFeatures() }

        // Then
        assertTrue(result.exceptionOrNull() is HttpException)
        coVerify(exactly = 0) { localDataSource.saveFeatures(any()) }
    }
}
