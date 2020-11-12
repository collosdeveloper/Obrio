package com.genesis.obrio.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.genesis.obrio.data.local.entity.Repository

@Dao
interface RepositoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(posts: List<Repository>)

    @Query("SELECT * FROM repos WHERE (name LIKE :queryString) ORDER BY stars DESC, name ASC LIMIT :limit")
    fun reposByName(queryString: String, limit: Int): LiveData<List<Repository>>
}