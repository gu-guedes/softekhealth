package com.example.softekhealth.data.remote.dto

/**
 * DTO para transferência de dados de dicas de bem-estar
 */
data class WellnessTipDto(
    val id: Int,
    val title: String,
    val shortDescription: String,
    val fullContent: String,
    val category: String
)
