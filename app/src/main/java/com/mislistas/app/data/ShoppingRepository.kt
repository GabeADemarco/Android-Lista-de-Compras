package com.mislistas.app.data

import kotlinx.coroutines.flow.Flow

class ShoppingRepository(private val dao: ShoppingListDao) {
    val listsWithItems: Flow<List<ListWithItems>> = dao.observeListsWithItems()

    suspend fun addList(name: String) {
        val sortOrder = dao.maxListSortOrder() + 1
        dao.insertList(ShoppingListEntity(name = name.trim(), sortOrder = sortOrder))
    }

    suspend fun updateList(list: ShoppingListEntity) {
        dao.updateList(list)
    }

    suspend fun deleteList(listId: Long) {
        dao.deleteList(listId)
    }

    suspend fun toggleListExpanded(list: ShoppingListEntity) {
        dao.updateList(list.copy(isExpanded = !list.isExpanded))
    }

    suspend fun addItem(
        listId: Long,
        name: String,
        quantity: String?,
        value: Double?,
    ) {
        val sortOrder = dao.maxItemSortOrder(listId) + 1
        dao.insertItem(
            ListItemEntity(
                listId = listId,
                name = name.trim(),
                quantity = quantity?.trim()?.takeIf { it.isNotEmpty() },
                value = value,
                sortOrder = sortOrder,
            ),
        )
    }

    suspend fun updateItem(item: ListItemEntity) {
        dao.updateItem(item)
    }

    suspend fun deleteItem(itemId: Long) {
        dao.deleteItem(itemId)
    }

    suspend fun toggleItemDone(item: ListItemEntity) {
        dao.updateItem(item.copy(isDone = !item.isDone))
    }
}
