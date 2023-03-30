package com.sefatombul.gcase.utils

object Constants {
    const val GITHUB_AUTH_BASE_URL = " https://github.com/"

    const val CLIENT_ID = "Iv1.83d65c1793c37c3c"
//    const val CLIENT_ID = "011853beec90f2afd2bb"
    const val CLIENT_SECRET = "bd439517066cd2f2b68cec4985f9dd641d9030b0"
//    const val CLIENT_SECRET = "aa1d0470357580e04973e76cff6fc5c790bd22a4"
    const val REDIRECT_URL = "https://wooglet.com/"
    const val SCOPE = "repo"

    const val AUTHORIZE_URL = "https://github.com/login/oauth/authorize?client_id=$CLIENT_ID&redirect_uri=$REDIRECT_URL&scope=$SCOPE"

    const val CACHE_MAX_SIZE = 10 * 1024 * 1024

    const val DB_ERROR_TEXT = "Veritabanından gelen sonuc yok. Tekrar Deneyiniz."
    const val UNKNOWN_ERROR = "Bilinmeyen Hata"
    const val CODE_401 = "CODE 401"
    const val SERVER_ERROR = "SERVER ERROR"
    const val NO_INTERNET_CONNECTION = "No internet connection"
}