package com.sefatombul.gcase.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sefatombul.gcase.utils.Constants

@Entity(tableName = Constants.RECENT_SEARCH_TABLE_NAME)
data class RecentSearchModel(
    @PrimaryKey(autoGenerate = true) var id: Int,
    var word: String,
    var type: String
)