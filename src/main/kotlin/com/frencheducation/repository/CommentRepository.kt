package com.frencheducation.repository

import com.frencheducation.data.model.comment.Comment
import com.frencheducation.data.tabel.CommentTable
import com.frencheducation.data.tabel.UserTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class CommentRepository {

    suspend fun addComment(comment: Comment) {
        DatabaseFactory.dbQuery {
            CommentTable.insert { commentTable ->
                commentTable[idUser] = comment.idUser
                commentTable[idCommunity] = comment.idCommunity
                commentTable[rating] = comment.rating
                commentTable[message] = comment.message
            }
        }
    }

    suspend fun getAllComments(id_community: Int): List<Comment> {
        return DatabaseFactory.dbQuery {
            (CommentTable innerJoin UserTable).select{
                CommentTable.idCommunity.eq(id_community)
            }.mapNotNull { rowToComment(it) }
        }
    }

    suspend fun updateComment(comment: Comment, id: Int) {
        DatabaseFactory.dbQuery {
            CommentTable.update(
                where = {
                    CommentTable.idComment.eq(id)
                }
            ) { commentTable ->
                commentTable[rating] = comment.rating
                commentTable[message] = comment.message
            }
        }
    }

    suspend fun deleteComment(id: Int) {
        DatabaseFactory.dbQuery {
            CommentTable.deleteWhere { CommentTable.idComment.eq(id) }
        }
    }

    suspend fun getCommentById(id: Int) = DatabaseFactory.dbQuery {
        (CommentTable innerJoin UserTable).select { CommentTable.idComment.eq(id) }
            .map { rowToComment(it) }
            .singleOrNull()
    }

    private fun rowToComment(row: ResultRow?): Comment? {
        if (row == null) {
            return null
        }

        return Comment(
            idComment = row[CommentTable.idComment],
            idCommunity = row[CommentTable.idCommunity],
            idUser = row[CommentTable.idUser],
            userName = row[UserTable.userName],
            userImage = row[UserTable.imageUrl],
            rating = row[CommentTable.rating],
            message = row[CommentTable.message]
        )
    }

}