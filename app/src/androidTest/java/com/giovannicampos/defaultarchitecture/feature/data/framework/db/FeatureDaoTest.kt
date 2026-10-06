package com.giovannicampos.defaultarchitecture.feature.data.framework.db

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.giovannicampos.defaultarchitecture.feature.data.model.FeatureEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FeatureDaoTest {

    private lateinit var database: FeatureDatabase
    private lateinit var dao: FeatureDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            FeatureDatabase::class.java,
        ).build()
        dao = database.featureDao()
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
    fun givenFeaturesSaved_whenGetAll_thenReturnsThem() = runTest {
        // Given
        val features = listOf(FeatureEntity(id = "1", name = "Primeira"), FeatureEntity(id = "2", name = "Segunda"))
        dao.upsertAll(features)

        // When
        val result = dao.getAll()

        // Then
        assertEquals(features.toSet(), result.toSet())
    }

    @Test
    fun givenFeatureAlreadySaved_whenUpsertWithSameId_thenReplacesIt() = runTest {
        // Given
        dao.upsertAll(listOf(FeatureEntity(id = "1", name = "Antiga")))

        // When
        dao.upsertAll(listOf(FeatureEntity(id = "1", name = "Nova")))

        // Then
        assertEquals(listOf(FeatureEntity(id = "1", name = "Nova")), dao.getAll())
    }
}
