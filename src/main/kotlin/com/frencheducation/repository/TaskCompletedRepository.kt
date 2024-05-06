package com.frencheducation.repository

import com.frencheducation.data.model.favorite_words.FavoriteWords
import com.frencheducation.data.model.task_completed.TaskCompleted
import com.frencheducation.data.tabel.FavoriteWordsTable
import com.frencheducation.data.tabel.TaskCompletedTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class TaskCompletedRepository {
    suspend fun addTaskCompleted(taskCompleted: TaskCompleted) {
        DatabaseFactory.dbQuery {
            TaskCompletedTable.insert { taskCompletedTable ->
                taskCompletedTable[idTask] = taskCompleted.idTask
                taskCompletedTable[idUser] = taskCompleted.idUser
            }
        }
    }

    suspend fun getAllTasksCompleted(id_user: Int): List<TaskCompleted> {
        return DatabaseFactory.dbQuery {
            TaskCompletedTable.select {
                TaskCompletedTable.idUser.eq(id_user)
            }.mapNotNull { rowToTaskCompleted(it) }
        }
    }

    private fun rowToTaskCompleted(row: ResultRow?): TaskCompleted? {
        if (row == null) {
            return null
        }

        return TaskCompleted(
            idTask = row[TaskCompletedTable.idTask],
            idUser = row[TaskCompletedTable.idUser]
        )
    }
}