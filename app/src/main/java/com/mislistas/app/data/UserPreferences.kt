package com.mislistas.app.data

enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK,
    ;

    fun next(): ThemeMode = when (this) {
        SYSTEM -> LIGHT
        LIGHT -> DARK
        DARK -> SYSTEM
    }
}

enum class ItemsViewMode {
    LIST,
    CHIPS,
    ;

    fun toggle(): ItemsViewMode = if (this == LIST) CHIPS else LIST
}
