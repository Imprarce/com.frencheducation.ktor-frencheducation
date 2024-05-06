package com.frencheducation.repository

import com.frencheducation.data.model.dictionary.Dictionary
import com.frencheducation.data.tabel.DictionaryTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class DictionaryRepository {
    suspend fun addWord(dictionary: Dictionary) {
        DatabaseFactory.dbQuery {
            DictionaryTable.insert { dictionaryTable ->
                dictionaryTable[wordLevel] = dictionary.wordLevel
                dictionaryTable[wordRu] = dictionary.wordRu
                dictionaryTable[wordFr] = dictionary.wordFr
            }
        }
    }

    suspend fun getAllWords(): List<Dictionary> {
        return DatabaseFactory.dbQuery {
            DictionaryTable.selectAll()
                .mapNotNull { rowToWord(it) }
        }
    }

    suspend fun updateWord(dictionary: Dictionary, id: Int) {
        DatabaseFactory.dbQuery {
            DictionaryTable.update(
                where = {
                    DictionaryTable.idWord.eq(id)
                }
            ) { dictionaryTable ->
                dictionaryTable[wordLevel] = dictionary.wordLevel
                dictionaryTable[wordRu] = dictionary.wordRu
                dictionaryTable[wordFr] = dictionary.wordFr
            }
        }
    }

    suspend fun deleteWord(id: Int) {
        DatabaseFactory.dbQuery {
            DictionaryTable.deleteWhere { DictionaryTable.idWord.eq(id) }
        }
    }

    suspend fun getWordById(id: Int) = DatabaseFactory.dbQuery {
        DictionaryTable.select { DictionaryTable.idWord.eq(id) }
            .map { rowToWord(it) }
            .singleOrNull()
    }

    private fun rowToWord(row: ResultRow?): Dictionary? {
        if (row == null) {
            return null
        }

        return Dictionary(
            idWord = row[DictionaryTable.idWord],
            wordLevel = row[DictionaryTable.wordLevel],
            wordRu = row[DictionaryTable.wordRu],
            wordFr = row[DictionaryTable.wordFr]
        )
    }

}