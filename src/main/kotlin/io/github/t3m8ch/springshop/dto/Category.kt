package io.github.t3m8ch.springshop.dto

import java.time.ZonedDateTime
import java.util.*

data class CategoryOutDTO(
    val id: UUID,
    val createdAt: ZonedDateTime,
    val updatedAt: ZonedDateTime,
    val name: String,
)

data class CreateUpdateCategoryDTO(
    val name: String,
)
