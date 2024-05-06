package com.frencheducation.data.model.task

import com.frencheducation.data.tabel.TaskTable
import com.frencheducation.data.tabel.TaskTable.autoIncrement

data class Task(
    var idTask: Int,
    var taskName: String,
    var videoFile: String,
    var audioFile: String,
    var exercise: String,
    var answer: String,
    var type: String
)
