package com.sefatombul.gcase.data.model

import com.google.gson.annotations.SerializedName

data class RevokeAccessRequestModel(
    @SerializedName("access_token") var accessToken: String? = null,
)