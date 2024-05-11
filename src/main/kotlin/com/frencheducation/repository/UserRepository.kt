package com.frencheducation.repository

import com.frencheducation.data.model.user.User
import com.frencheducation.data.tabel.UserTable
import com.frencheducation.repository.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update

class UserRepository {

    suspend fun addUser(user: User) {
        dbQuery {
            UserTable.insert { userTable ->
                userTable[UserTable.email] = user.email
                userTable[UserTable.hashPassword] = user.hashPassword
                userTable[UserTable.userName] = user.userName
                userTable[UserTable.imageUrl] = user.imageUrl
                userTable[UserTable.dateCreateAcc] = user.dateCreateAcc
            }
        }
    }

    suspend fun findUserByEmail(email: String) = dbQuery {
        UserTable.select { UserTable.email.eq(email) }
            .map { rowToUser(it) }
            .singleOrNull()
    }

    suspend fun updateUserImage(userId: Int, newImageUrl: String) {
        dbQuery {
            UserTable.update({ UserTable.idUser eq userId }) {
                it[imageUrl] = newImageUrl
            }
        }
    }

    private fun rowToUser(row: ResultRow?): User?{
        if(row == null){
            return null
        }

        return User(
            idUser = row[UserTable.idUser],
            email = row[UserTable.email],
            hashPassword = row[UserTable.hashPassword],
            userName = row[UserTable.userName],
            imageUrl = row[UserTable.imageUrl],
            dateCreateAcc = row[UserTable.dateCreateAcc]
        )
    }
}