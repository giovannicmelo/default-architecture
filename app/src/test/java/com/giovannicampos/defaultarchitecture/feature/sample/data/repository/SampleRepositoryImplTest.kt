package com.giovannicampos.defaultarchitecture.feature.sample.data.repository

import com.giovannicampos.defaultarchitecture.feature.sample.data.model.SampleEntity
import com.giovannicampos.defaultarchitecture.feature.sample.data.source.local.SampleLocalDataSource
import com.giovannicampos.defaultarchitecture.feature.sample.data.source.remote.SampleRemoteDataSourceImpl
import com.giovannicampos.defaultarchitecture.feature.sample.domain.model.Sample
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

class SampleRepositoryImplTest {

    private val server = MockWebServer()
    private val json = Json { ignoreUnknownKeys = true }
    private val localDataSource = mockk<SampleLocalDataSource>(relaxUnitFun = true)
    private lateinit var repository: SampleRepositoryImpl

    @Before
    fun setUp() {
        server.start()
        val retrofit = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
        repository = SampleRepositoryImpl(
            remoteDataSource = SampleRemoteDataSourceImpl(retrofit.create()),
            localDataSource = localDataSource,
        )
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `given api returns samples, when getSamples, then saves them locally and returns local samples`() = runTest {
        // Given
        server.enqueue(MockResponse().setResponseCode(200).setBody("""[{"id":"1","name":"Primeira"}]"""))
        coEvery { localDataSource.getSamples() } returns listOf(SampleEntity(id = "1", name = "Primeira"))

        // When
        val result = repository.getSamples()

        // Then
        assertEquals("/samples", server.takeRequest().path)
        coVerify { localDataSource.saveSamples(listOf(SampleEntity(id = "1", name = "Primeira"))) }
        assertEquals(listOf(Sample(id = "1", name = "Primeira")), result)
    }

    @Test
    fun `given network is unavailable, when getSamples, then returns cached samples without saving`() = runTest {
        // Given
        server.enqueue(MockResponse().setSocketPolicy(SocketPolicy.DISCONNECT_AT_START))
        coEvery { localDataSource.getSamples() } returns listOf(SampleEntity(id = "1", name = "Em cache"))

        // When
        val result = repository.getSamples()

        // Then
        coVerify(exactly = 0) { localDataSource.saveSamples(any()) }
        assertEquals(listOf(Sample(id = "1", name = "Em cache")), result)
    }

    @Test
    fun `given api returns server error, when getSamples, then throws HttpException`() = runTest {
        // Given
        server.enqueue(MockResponse().setResponseCode(500))

        // When
        val result = runCatching { repository.getSamples() }

        // Then
        assertTrue(result.exceptionOrNull() is HttpException)
        coVerify(exactly = 0) { localDataSource.saveSamples(any()) }
    }
}
