package com.frencheducation.repository

import com.frencheducation.data.tabel.*
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {

    fun init(){
        Database.connect(hikari())

        transaction {
            SchemaUtils.create(UserTable)
            SchemaUtils.create(CommentTable)
            SchemaUtils.create(CommunityTable)
            SchemaUtils.create(DictionaryTable)
            SchemaUtils.create(FavoriteWordsTable)
            SchemaUtils.create(ModuleProgressTable)
            SchemaUtils.create(ModuleTable)
            SchemaUtils.create(ModuleTasksTable)
            SchemaUtils.create(TaskCompletedTable)
            SchemaUtils.create(TaskTable)
            SchemaUtils.create(VideoTable)

        }
    }
    private fun hikari(): HikariDataSource{
        val config = HikariConfig()
        config.driverClassName = System.getenv("JDBC_DRIVER")
        config.jdbcUrl = System.getenv("LOCAL_DATABASE_URL")
//        config.jdbcUrl = System.getenv("DATABASE_URL")
//        config.username = System.getenv("DB_USER")
//        config.password = System.getenv("DB_PASSWORD")
        config.maximumPoolSize = 3
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.validate()

        return HikariDataSource(config)
    }

    suspend fun <T> dbQuery(block: () -> T): T =
        withContext(Dispatchers.IO){
            transaction { block() }
        }
}