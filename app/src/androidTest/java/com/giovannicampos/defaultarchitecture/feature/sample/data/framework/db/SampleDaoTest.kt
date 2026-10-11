package com.giovannicampos.defaultarchitecture.feature.sample.data.framework.db

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.giovannicampos.defaultarchitecture.feature.sample.data.model.SampleEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SampleDaoTest {

    private lateinit var database: SampleDatabase
    private lateinit var dao: SampleDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            SampleDatabase::class.java,
        ).build()
        dao = database.sampleDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun givenEmptyDatabase_whenGetAll_thenReturnsEmptyList() = runTest {
        // Given
        // banco recém-criado, sem registros

        // When
        val result = dao.getAll()

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun givenSamplesSaved_whenGetAll_thenReturnsThem() = runTest {
        // Given
        val samples = listOf(SampleEntity(id = "1", name = "Primeira"), SampleEntity(id = "2", name = "Segunda"))
        dao.upsertAll(samples)

        // When
        val result = dao.getAll()

        // Then
        assertEquals(samples.toSet(), result.toSet())
    }

    @Test
    fun givenSampleAlreadySaved_whenUpsertWithSameId_thenReplacesIt() = runTest {
        // Given
        dao.upsertAll(listOf(SampleEntity(id = "1", name = "Antiga")))

        // When
        dao.upsertAll(listOf(SampleEntity(id = "1", name = "Nova")))

        // Then
        assertEquals(listOf(SampleEntity(id = "1", name = "Nova")), dao.getAll())
    }
}
