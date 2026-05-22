package com.hanikorm.spacehub2.data.repository

import android.content.Context
import com.hanikorm.spacehub2.R
import com.google.gson.reflect.TypeToken
import com.google.gson.Gson
import com.hanikorm.spacehub2.domain.model.ApodEntry
import com.hanikorm.spacehub2.domain.model.ApodItemDto
import kotlin.collections.map

class ApodRepository(private val context: Context) {

    fun getOfflineApods(): List<ApodEntry> {
        return try {
            // 1. Открываем поток данных
            val inputStream = context.resources.openRawResource(R.raw.apod_final)

            // 2. Читаем текст
            val jsonString = inputStream.bufferedReader().use { it.readText() }

            // 3. Создаем тип для Gson (указываем полные пути, чтобы не было ошибок)
            val listType = object : TypeToken<List<ApodItemDto>>() {}.type

            // 4. Парсим JSON в список DTO
            val dtos: List<ApodItemDto> = Gson().fromJson(jsonString, listType)

            // 5. Превращаем DTO в Entry для экрана
            dtos.map { dto ->
                ApodEntry(
                    id = dto.id,
                    title = dto.title,
                    explanation = dto.explanation,
                    imageResName = dto.url,
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}