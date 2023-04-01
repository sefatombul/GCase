package com.sefatombul.gcase.utils

object Constants {

    const val PREFERENCES_NAME = "gcase_preference"

    const val ACCESS_TOKEN_URL = "https://wooglet.com/callback.txt"

    const val ACCESS_TOKEN = "ACCESS_TOKEN"
    const val REFRESH_TOKEN = "REFRESH_TOKEN"

    const val CLIENT_ID = "Iv1.83d65c1793c37c3c"
    const val CLIENT_SECRET = "bd439517066cd2f2b68cec4985f9dd641d9030b0"
    const val REDIRECT_URL = "https://wooglet.com/callback"
    const val SCOPE = "repo,read:org"

    const val GITHUB_AUTH_BASE_URL = "https://github.com/"
    const val GITHUB_API_BASE_URL = "https://api.github.com/"
    const val WOOGLET_BASE_URL = "https://wooglet.com/"
    const val REVOKE_ACCESS_URL = "https://api.github.com/applications/$CLIENT_ID/grant"

    const val AUTHORIZE_URL =
        "https://github.com/login/oauth/authorize?client_id=$CLIENT_ID&redirect_uri=$REDIRECT_URL&scope=$SCOPE"

    const val CACHE_MAX_SIZE = 10 * 1024 * 1024

    const val DB_ERROR_TEXT = "Veritabanından gelen sonuc yok. Tekrar Deneyiniz."
    const val UNKNOWN_ERROR = "Bilinmeyen Hata"
    const val CODE_401 = "CODE 401"
    const val SERVER_ERROR = "SERVER ERROR"
    const val NO_INTERNET_CONNECTION = "No internet connection"
}