package com.frencheducation.repository

import com.frencheducation.data.model.task.Task
import com.frencheducation.data.tabel.TaskTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class TaskRepository {


    suspend fun addTask(task: Task) {
        DatabaseFactory.dbQuery {
            TaskTable.insert { taskTable ->
                taskTable[taskName] = task.taskName
                taskTable[videoFile] = task.videoFile
                taskTable[audioFile] = task.audioFile
                taskTable[exercise] = task.exercise
                taskTable[answer] = task.answer
                taskTable[type] = task.type
            }
        }
    }

    suspend fun getAllTasks(): List<Task> {
        return DatabaseFactory.dbQuery {
            TaskTable.selectAll()
                .mapNotNull { rowToTask(it) }
        }
    }

    suspend fun updateTask(task: Task, id: Int) {
        DatabaseFactory.dbQuery {
            TaskTable.update(
                where = {
                    TaskTable.idTask.eq(id)
                }
            ) { taskTable ->
                taskTable[taskName] = task.taskName
                taskTable[videoFile] = task.videoFile
                taskTable[audioFile] = task.audioFile
                taskTable[exercise] = task.exercise
                taskTable[answer] = task.answer
                taskTable[type] = task.type
            }
        }
    }

    suspend fun deleteTask(id: Int){
        DatabaseFactory.dbQuery {
            TaskTable.deleteWhere { TaskTable.idTask.eq(id) }
        }
    }

    suspend fun getTaskById(id: Int) = DatabaseFactory.dbQuery {
        TaskTable.select { TaskTable.idTask.eq(id) }
            .map { rowToTask(it) }
            .singleOrNull()
    }

    private fun rowToTask(row: ResultRow?): Task? {
        if (row == null) {
            return null
        }

        return Task(
            idTask = row[TaskTable.idTask],
            taskName = row[TaskTable.taskName],
            videoFile = row[TaskTable.videoFile],
            audioFile = row[TaskTable.audioFile],
            exercise = row[TaskTable.exercise],
            answer = row[TaskTable.answer],
            type = row[TaskTable.type]
        )
    }
}