package com.frencheducation.data.tabel

import com.frencheducation.data.tabel.ModuleTable.autoIncrement
import org.jetbrains.exposed.sql.Table

object DictionaryTable : Table("dictionary") {
    var idWord = integer("id_word").autoIncrement()
    var wordLevel = integer("word_level")
    var wordRu = varchar("rus_translate", 60)
    var wordFr = varchar("fr_translate", 60)

    override val primaryKey: PrimaryKey = PrimaryKey(idWord)
}