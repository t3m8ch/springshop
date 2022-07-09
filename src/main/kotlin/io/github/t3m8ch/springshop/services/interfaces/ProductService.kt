package io.github.t3m8ch.springshop.services.interfaces

import io.github.t3m8ch.springshop.dto.CreateUpdateProductDTO
import io.github.t3m8ch.springshop.dto.ProductOutDTO
import java.util.*

interface ProductService {
    fun getAll(categoryId: UUID? = null): List<ProductOutDTO>
    fun getById(id: UUID): ProductOutDTO
    fun create(dto: CreateUpdateProductDTO): ProductOutDTO
    fun updateById(id: UUID, dto: CreateUpdateProductDTO): ProductOutDTO
    fun removeById(id: UUID): ProductOutDTO
    fun deleteById(id: UUID): ProductOutDTO
    fun restoreById(id: UUID): ProductOutDTO
}
