package com.mislistas.app.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferencesRepository(private val context: Context) {
    val themeMode: Flow<ThemeMode> = context.dataStore.data.map { prefs ->
        ThemeMode.entries.find { it.name == prefs[KEY_THEME_MODE] } ?: ThemeMode.SYSTEM
    }

    val itemsViewMode: Flow<ItemsViewMode> = context.dataStore.data.map { prefs ->
        ItemsViewMode.entries.find { it.name == prefs[KEY_ITEMS_VIEW_MODE] } ?: ItemsViewMode.LIST
    }

    suspend fun setThemeMode(mode: ThemeMode) {
        context.dataStore.edit { it[KEY_THEME_MODE] = mode.name }
    }

    suspend fun setItemsViewMode(mode: ItemsViewMode) {
        context.dataStore.edit { it[KEY_ITEMS_VIEW_MODE] = mode.name }
    }

    companion object {
        private val KEY_THEME_MODE = stringPreferencesKey("theme_mode")
        private val KEY_ITEMS_VIEW_MODE = stringPreferencesKey("items_view_mode")
    }
}
