package io.github.t3m8ch.springshop

import io.github.t3m8ch.springshop.dto.CategoryOutDTO
import io.github.t3m8ch.springshop.entities.CategoryEntity

fun CategoryEntity.mapToOutDTO(): CategoryOutDTO = CategoryOutDTO(id, createdAt!!, updatedAt!!, name)
