package com.mislistas.app.data

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

data class ListWithItems(
    @Embedded val list: ShoppingListEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "listId",
    )
    val items: List<ListItemEntity>,
)

@Dao
interface ShoppingListDao {
    @Transaction
    @Query("SELECT * FROM shopping_lists ORDER BY sortOrder ASC, id ASC")
    fun observeListsWithItems(): Flow<List<ListWithItems>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(list: ShoppingListEntity): Long

    @Update
    suspend fun updateList(list: ShoppingListEntity)

    @Query("DELETE FROM shopping_lists WHERE id = :listId")
    suspend fun deleteList(listId: Long)

    @Query("SELECT COALESCE(MAX(sortOrder), -1) FROM shopping_lists")
    suspend fun maxListSortOrder(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ListItemEntity): Long

    @Update
    suspend fun updateItem(item: ListItemEntity)

    @Query("DELETE FROM list_items WHERE id = :itemId")
    suspend fun deleteItem(itemId: Long)

    @Query("SELECT COALESCE(MAX(sortOrder), -1) FROM list_items WHERE listId = :listId")
    suspend fun maxItemSortOrder(listId: Long): Int
}
