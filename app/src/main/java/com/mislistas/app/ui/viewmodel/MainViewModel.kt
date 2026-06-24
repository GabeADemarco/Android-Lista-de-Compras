package com.mislistas.app.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mislistas.app.data.AppDatabase
import com.mislistas.app.data.ListItemEntity
import com.mislistas.app.data.ShoppingListEntity
import com.mislistas.app.data.ShoppingRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ShoppingRepository(
        AppDatabase.getInstance(application).shoppingListDao(),
    )

    val listsWithItems = repository.listsWithItems

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
