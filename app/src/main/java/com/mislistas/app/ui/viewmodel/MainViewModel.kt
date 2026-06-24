package com.mislistas.app.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mislistas.app.data.AppDatabase
import com.mislistas.app.data.ItemsViewMode
import com.mislistas.app.data.ListItemEntity
import com.mislistas.app.data.ShoppingListEntity
import com.mislistas.app.data.ShoppingRepository
import com.mislistas.app.data.ThemeMode
import com.mislistas.app.data.UserPreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ShoppingRepository(
        AppDatabase.getInstance(application).shoppingListDao(),
    )
    private val preferences = UserPreferencesRepository(application)

    val listsWithItems = repository.listsWithItems

    val themeMode = preferences.themeMode.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ThemeMode.SYSTEM,
    )

    val itemsViewMode = preferences.itemsViewMode.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ItemsViewMode.LIST,
    )

    fun cycleThemeMode() {
        viewModelScope.launch {
            val current = themeMode.value
            preferences.setThemeMode(current.next())
        }
    }

    fun toggleItemsViewMode() {
        viewModelScope.launch {
            val current = itemsViewMode.value
            preferences.setItemsViewMode(current.toggle())
        }
    }

    fun addList(name: String) {
        if (name.isBlank()) return
        viewModelScope.launch { repository.addList(name) }
    }

    fun updateList(list: ShoppingListEntity) {
        viewModelScope.launch { repository.updateList(list) }
    }

    fun deleteList(listId: Long) {
        viewModelScope.launch { repository.deleteList(listId) }
    }

    fun toggleListExpanded(list: ShoppingListEntity) {
        viewModelScope.launch { repository.toggleListExpanded(list) }
    }

    fun addItem(listId: Long, name: String, quantity: String?, value: Double?) {
        if (name.isBlank()) return
        viewModelScope.launch { repository.addItem(listId, name, quantity, value) }
    }

    fun updateItem(item: ListItemEntity) {
        viewModelScope.launch { repository.updateItem(item) }
    }

    fun deleteItem(itemId: Long) {
        viewModelScope.launch { repository.deleteItem(itemId) }
    }

    fun toggleItemDone(item: ListItemEntity) {
        viewModelScope.launch { repository.toggleItemDone(item) }
    }
}
