package com.frencheducation.repository

import com.frencheducation.data.model.video.Video
import com.frencheducation.data.tabel.VideoTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq


class VideoRepository {

    suspend fun addVideo(video: Video) {
        DatabaseFactory.dbQuery {
            VideoTable.insert { videoTable ->
                videoTable[rating] = video.rating
                videoTable[view] = video.view
                videoTable[description] = video.description
                videoTable[title] = video.title
                videoTable[videoFile] = video.videoFile
            }
        }
    }

    suspend fun getAllVideos(): List<Video> {
        return DatabaseFactory.dbQuery {
            VideoTable.selectAll()
                .mapNotNull { rowToVideo(it) }
        }
    }

    suspend fun updateVideo(video: Video, id: Int) {
        DatabaseFactory.dbQuery {
            VideoTable.update(
                where = {
                    VideoTable.idVideo.eq(id)
                }
            ) { videoTable ->
                videoTable[rating] = video.rating
                videoTable[view] = video.view
                videoTable[description] = video.description
                videoTable[title] = video.title
                videoTable[videoFile] = video.videoFile
            }
        }
    }

    suspend fun deleteVideo(id: Int) {
        DatabaseFactory.dbQuery {
            VideoTable.deleteWhere { VideoTable.idVideo.eq(id) }
        }
    }

    suspend fun getVideoById(id: Int) = DatabaseFactory.dbQuery {
        VideoTable.select { VideoTable.idVideo.eq(id) }
            .map { rowToVideo(it) }
            .singleOrNull()
    }

    private fun rowToVideo(row: ResultRow?): Video? {
        if (row == null) {
            return null
        }

        return Video(
            videoId = row[VideoTable.idVideo],
            userId = row[VideoTable.idUser],
            rating = row[VideoTable.rating],
            view = row[VideoTable.view],
            description = row[VideoTable.description],
            title = row[VideoTable.title],
            videoFile = row[VideoTable.videoFile],
        )
    }

}