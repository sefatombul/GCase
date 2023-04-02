package com.sefatombul.gcase.data.repository

import com.sefatombul.gcase.data.local.RecentSearchDao
import com.sefatombul.gcase.data.local.RecentSearchModel
import javax.inject.Inject

class RecentSearchRepository @Inject constructor(
    private val recentSearchDao: RecentSearchDao
) {
    suspend fun deleteRecentSearchWithWordText(word: String, type: String) =
        recentSearchDao.deleteWithWordText(word, type)

    suspend fun insertRecentSearch(recentSearchModel: RecentSearchModel) =
        recentSearchDao.insert(recentSearchModel)

    suspend fun getRecentSearchLocal(limit: Int, type: String) =
        recentSearchDao.getRecentSearchLocal(limit, type)

    suspend fun deleteAllText(type: String) = recentSearchDao.deleteAllTextType(type)
}