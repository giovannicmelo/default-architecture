package com.giovannicampos.defaultarchitecture.feature.data.framework.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.giovannicampos.defaultarchitecture.feature.data.model.FeatureEntity

@Database(entities = [FeatureEntity::class], version = 1, exportSchema = false)
abstract class FeatureDatabase : RoomDatabase() {

    abstract fun featureDao(): FeatureDao
}
