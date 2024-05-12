package com.frencheducation.repository

import com.frencheducation.data.model.task_completed.TaskCompleted
import com.frencheducation.data.tabel.TaskCompletedTable
import com.frencheducation.repository.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class TaskCompletedRepository {
    suspend fun addTaskCompleted(taskCompleted: TaskCompleted) {
        dbQuery {
            TaskCompletedTable.insert { taskCompletedTable ->
                taskCompletedTable[idTask] = taskCompleted.idTask
                taskCompletedTable[idUser] = taskCompleted.idUser
            }
        }
    }

    suspend fun getTaskCompleted(id_user: Int, id_task: Int) = dbQuery {
        TaskCompletedTable.select {
            TaskCompletedTable.idUser.eq(id_user) and TaskCompletedTable.idTask.eq(id_task)
        }
            .mapNotNull { rowToTaskCompleted(it) }
            .singleOrNull()
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