package com.frencheducation.repository

import com.frencheducation.data.model.dictionary.Dictionary
import com.frencheducation.data.model.favorite_words.FavoriteWords
import com.frencheducation.data.tabel.DictionaryTable
import com.frencheducation.data.tabel.FavoriteWordsTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class FavoriteWordsRepository {

    suspend fun addFavoriteWord(favoriteWords: FavoriteWords) {
        DatabaseFactory.dbQuery {
            FavoriteWordsTable.insert { favoriteWordsTable ->
                favoriteWordsTable[idWord] = favoriteWords.idWord
                favoriteWordsTable[idUser] = favoriteWords.idUser
            }
        }
    }

    suspend fun getAllFavoriteWords(id_user: Int): List<FavoriteWords> {
        return DatabaseFactory.dbQuery {
            FavoriteWordsTable.select{
                FavoriteWordsTable.idUser.eq(id_user)
            }.mapNotNull { rowToFavoriteWords(it) }
        }
    }

    suspend fun deleteFavoriteWord(id_user: Int, id_word: Int) {
        DatabaseFactory.dbQuery {
            FavoriteWordsTable.deleteWhere { FavoriteWordsTable.idUser.eq(id_user) and FavoriteWordsTable.idWord.eq(id_word) }
        }
    }

    private fun rowToFavoriteWords(row: ResultRow?): FavoriteWords? {
        if (row == null) {
            return null
        }

        return FavoriteWords(
            idWord = row[FavoriteWordsTable.idWord],
            idUser = row[FavoriteWordsTable.idUser]
        )
    }
}