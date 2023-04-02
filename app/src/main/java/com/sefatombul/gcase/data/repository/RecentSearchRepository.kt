package com.sefatombul.gcase.data.repository

import com.sefatombul.gcase.data.local.RecentSearchDao
import com.sefatombul.gcase.data.local.RecentSearchModel
import com.sefatombul.gcase.data.remote.ApiService
import javax.inject.Inject

class RecentSearchRepository @Inject constructor(
    private val recentSearchDao: RecentSearchDao
) {
    suspend fun deleteRecentSearchWithWordText(word: String) =
        recentSearchDao.deleteWithWordText(word)

    suspend fun insertRecentSearch(recentSearchModel: RecentSearchModel) =
        recentSearchDao.insert(recentSearchModel)

    suspend fun getRecentSearchLocal(limit: Int) = recentSearchDao.getRecentSearchLocal(limit)

    suspend fun deleteAllText() = recentSearchDao.deleteAllText()
}