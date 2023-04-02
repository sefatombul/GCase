package com.sefatombul.gcase.data.local

import androidx.room.*
import com.sefatombul.gcase.utils.Constants


@Dao
interface RecentSearchDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recentSearchModel: RecentSearchModel): Long

    @Update
    suspend fun update(recentSearchModel: RecentSearchModel): Int

    @Delete
    suspend fun delete(recentSearchModel: RecentSearchModel): Int

    @Query("Select * from ${Constants.RECENT_SEARCH_TABLE_NAME} order by id desc limit :limit ")
    suspend fun getRecentSearchLocal(limit: Int): List<RecentSearchModel>

    @Query("delete from ${Constants.RECENT_SEARCH_TABLE_NAME} where word = :word")
    suspend fun deleteWithWordText(word: String): Int

    @Query("delete from ${Constants.RECENT_SEARCH_TABLE_NAME}")
    suspend fun deleteAllText(): Int
}