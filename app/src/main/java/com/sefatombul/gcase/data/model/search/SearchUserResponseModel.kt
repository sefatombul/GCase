package com.sefatombul.gcase.data.model.search

import com.google.gson.annotations.SerializedName

data class SearchUserResponseModel(
    @SerializedName("total_count") var totalCount: Int? = null,
    @SerializedName("incomplete_results") var incompleteResults: Boolean? = null,
    @SerializedName("items") var items: ArrayList<UserResponseItems> = arrayListOf()
)