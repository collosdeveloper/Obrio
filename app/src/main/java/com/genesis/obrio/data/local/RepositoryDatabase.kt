package com.genesis.obrio.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.genesis.obrio.data.local.entity.Repository

@Database(
    entities = [Repository::class],
    version = 1,
    exportSchema = false
)
abstract class RepositoryDatabase : RoomDatabase() {

    abstract fun repositoryDao(): RepositoryDao

    companion object {

        operator fun invoke(context: Context): RepositoryDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        @Volatile
        private var INSTANCE: RepositoryDatabase? = null

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                RepositoryDatabase::class.java, "repository.db")
                .build()
    }
}