package com.frencheducation.data.tabel

import org.jetbrains.exposed.sql.Table

object TaskTable : Table("task") {
    var idTask = integer("id_task").autoIncrement()
    var taskName = varchar("task_name", 60)
    var videoFile = varchar("video_file", 300)
    var audioFile = varchar("audio_file", 300)
    var exercise = varchar("exercise", 300)
    var answer = varchar("answer", 300)
    var type = varchar("type", 100)

    override val primaryKey: PrimaryKey = PrimaryKey(idTask)
}
