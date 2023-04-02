package com.sefatombul.gcase.data.model

import com.google.gson.annotations.SerializedName

data class Plan(
    @SerializedName("name") var name: String? = null,
    @SerializedName("space") var space: Int? = null,
    @SerializedName("collaborators") var collaborators: Int? = null,
    @SerializedName("private_repos") var privateRepos: Int? = null
)