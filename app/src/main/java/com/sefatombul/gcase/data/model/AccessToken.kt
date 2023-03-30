package com.sefatombul.gcase.data.model

import com.google.gson.annotations.SerializedName

data class AccessToken(
    @SerializedName("access_token"             ) var accessToken           : String? = null,
    @SerializedName("expires_in"               ) var expiresIn             : Int?    = null,
    @SerializedName("refresh_token"            ) var refreshToken          : String? = null,
    @SerializedName("refresh_token_expires_in" ) var refreshTokenExpiresIn : Int?    = null,
    @SerializedName("token_type"               ) var tokenType             : String? = null,
    @SerializedName("scope"                    ) var scope                 : String? = null,
    @SerializedName("error"                    ) var error                 : String? = null
)