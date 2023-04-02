package com.sefatombul.gcase.data.model.search

data class Sort(
    var text: String,
    var key: String? = null,
    var isChecked: Boolean = false
)