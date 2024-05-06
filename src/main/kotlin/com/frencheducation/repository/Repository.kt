package com.frencheducation.repository

import com.frencheducation.data.model.user.User
import com.frencheducation.data.tabel.UserTable
import com.frencheducation.repository.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class Repository {

    suspend fun addUser(user: User) {
        dbQuery {
            UserTable.insert { ut ->
                ut[UserTable.email] = user.email
                ut[UserTable.hashPassword] = user.hashPassword
                ut[UserTable.userName] = user.userName
                ut[UserTable.imageUrl] = user.imageUrl
                ut[UserTable.dateCreateAcc] = user.dateCreateAcc
            }
        }
    }

    suspend fun findUserByEmail(email: String) = dbQuery {
        UserTable.select { UserTable.email.eq(email) }
            .map { rowToUser(it) }
            .singleOrNull()
    }

    private fun rowToUser(row: ResultRow?): User?{
        if(row == null){
            return null
        }

        return User(
            id = row[UserTable.idUser],
            email = row[UserTable.email],
            hashPassword = row[UserTable.hashPassword],
            userName = row[UserTable.userName],
            imageUrl = row[UserTable.imageUrl],
            dateCreateAcc = row[UserTable.dateCreateAcc]
        )
    }
}