package io.github.t3m8ch.springshop.dto

import java.io.Serializable
import java.math.BigDecimal
import java.time.ZonedDateTime
import java.util.*

typealias CharacteristicsType = Map<String, Serializable>

data class ProductOutDTO(
    val id: UUID,
    val createdAt: ZonedDateTime,
    val updatedAt: ZonedDateTime,
    val title: String,
    val description: String,
    val price: BigDecimal,
    val characteristics: CharacteristicsType,
    val category: CategoryInProductDTO,
)

data class CategoryInProductDTO(
    val id: UUID,
    val name: String,
)

data class CreateUpdateProductDTO(
    val title: String,
    val description: String,
    val price: BigDecimal,
    val characteristics: CharacteristicsType,
    val categoryId: UUID,
)
