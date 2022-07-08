package io.github.t3m8ch.springshop.services.interfaces

import io.github.t3m8ch.springshop.dto.CategoryOutDTO
import io.github.t3m8ch.springshop.dto.CreateUpdateCategoryDTO
import java.util.*

interface CategoryService {
    fun getAll(): List<CategoryOutDTO>
    fun getById(id: UUID): CategoryOutDTO
    fun create(dto: CreateUpdateCategoryDTO): CategoryOutDTO
    fun updateById(id: UUID, dto: CreateUpdateCategoryDTO): CategoryOutDTO
    fun remove(id: UUID): CategoryOutDTO
    fun delete(id: UUID): CategoryOutDTO
}
