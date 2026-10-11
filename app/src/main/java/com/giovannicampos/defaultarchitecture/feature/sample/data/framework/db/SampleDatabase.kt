package com.giovannicampos.defaultarchitecture.feature.sample.data.framework.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.giovannicampos.defaultarchitecture.feature.sample.data.model.SampleEntity

@Database(entities = [SampleEntity::class], version = 1, exportSchema = false)
abstract class SampleDatabase : RoomDatabase() {

    abstract fun sampleDao(): SampleDao
}
