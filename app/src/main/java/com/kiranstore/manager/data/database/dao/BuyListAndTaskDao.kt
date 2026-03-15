package com.kiranstore.manager.data.database.dao

import androidx.room.*
import com.kiranstore.manager.data.database.entities.BuyListItemEntity
import com.kiranstore.manager.data.database.entities.TaskEntity
import kotlinx.coroutines.flow.Flow

// ─────────────────────────────────────────────
// BUY LIST
// ─────────────────────────────────────────────
@Dao
interface BuyListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: BuyListItemEntity): Long

    @Update
    suspend fun updateItem(item: BuyListItemEntity)

    @Delete
    suspend fun deleteItem(item: BuyListItemEntity)

    @Query("SELECT * FROM buy_list_items WHERE shopId = :shopId AND isPurchased = 0 ORDER BY priority DESC, createdAt ASC")
    fun getPendingItems(shopId: Long): Flow<List<BuyListItemEntity>>

    @Query("SELECT * FROM buy_list_items WHERE shopId = :shopId ORDER BY isPurchased ASC, priority DESC")
    fun getAllItems(shopId: Long): Flow<List<BuyListItemEntity>>

    @Query("SELECT COUNT(*) FROM buy_list_items WHERE shopId = :shopId AND isPurchased = 0")
    fun getPendingCount(shopId: Long): Flow<Int>

    @Query("UPDATE buy_list_items SET isPurchased = 1 WHERE id = :itemId")
    suspend fun markAsPurchased(itemId: Long)

    @Query("DELETE FROM buy_list_items WHERE shopId = :shopId AND isPurchased = 1")
    suspend fun clearPurchased(shopId: Long)
}

// ─────────────────────────────────────────────
// TASK
// ─────────────────────────────────────────────
@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity): Long

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Query("SELECT * FROM tasks WHERE shopId = :shopId ORDER BY date DESC")
    fun getAllTasks(shopId: Long): Flow<List<TaskEntity>>

    @Query("""
        SELECT * FROM tasks 
        WHERE shopId = :shopId 
        AND date >= :dayStart AND date <= :dayEnd
        ORDER BY status ASC
    """)
    fun getTasksForDay(shopId: Long, dayStart: Long, dayEnd: Long): Flow<List<TaskEntity>>

    @Query("""
        SELECT COUNT(*) FROM tasks 
        WHERE shopId = :shopId AND status = 'PENDING'
        AND date >= :dayStart AND date <= :dayEnd
    """)
    fun getTodayPendingCount(shopId: Long, dayStart: Long, dayEnd: Long): Flow<Int>

    @Query("UPDATE tasks SET status = 'DONE' WHERE id = :taskId")
    suspend fun markAsDone(taskId: Long)
}
