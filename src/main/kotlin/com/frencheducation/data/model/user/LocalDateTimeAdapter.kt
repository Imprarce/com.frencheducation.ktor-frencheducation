package com.frencheducation.data.model.user

import com.google.gson.*
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class LocalDateTypeAdapter : JsonSerializer<LocalDate?>, JsonDeserializer<LocalDate?> {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    @Throws(JsonParseException::class)
    override fun deserialize(
        json: JsonElement, typeOfT: Type?,
        context: JsonDeserializationContext?
    ): LocalDate {
        return LocalDate.parse(json.asString, formatter)
    }

    override fun serialize(src: LocalDate?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(src!!.format(formatter))
    }
}