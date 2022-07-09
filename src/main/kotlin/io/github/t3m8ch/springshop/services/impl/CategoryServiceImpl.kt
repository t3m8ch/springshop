package io.github.t3m8ch.springshop.services.impl

import io.github.t3m8ch.springshop.dto.CategoryOutDTO
import io.github.t3m8ch.springshop.dto.CreateUpdateCategoryDTO
import io.github.t3m8ch.springshop.services.interfaces.CategoryService
import org.springframework.stereotype.Service
import java.util.*

@Service
class CategoryServiceImpl : CategoryService {
    override fun getAll(): List<CategoryOutDTO> {
        TODO("Not yet implemented")
    }

    override fun getById(id: UUID): CategoryOutDTO {
        TODO("Not yet implemented")
    }

    override fun create(dto: CreateUpdateCategoryDTO): CategoryOutDTO {
        TODO("Not yet implemented")
    }

    override fun updateById(id: UUID, dto: CreateUpdateCategoryDTO): CategoryOutDTO {
        TODO("Not yet implemented")
    }

    override fun remove(id: UUID): CategoryOutDTO {
        TODO("Not yet implemented")
    }

    override fun delete(id: UUID): CategoryOutDTO {
        TODO("Not yet implemented")
    }

    override fun restoreById(id: UUID): CategoryOutDTO {
        TODO("Not yet implemented")
    }
}
