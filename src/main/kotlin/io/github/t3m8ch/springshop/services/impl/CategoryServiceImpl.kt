package io.github.t3m8ch.springshop.services.impl

import io.github.t3m8ch.springshop.dto.CategoryOutDTO
import io.github.t3m8ch.springshop.dto.CreateUpdateCategoryDTO
import io.github.t3m8ch.springshop.entities.CategoryEntity
import io.github.t3m8ch.springshop.exceptions.category.CategoryIsNotRemovedException
import io.github.t3m8ch.springshop.exceptions.category.CategoryIsRemovedException
import io.github.t3m8ch.springshop.exceptions.category.CategoryNotFoundException
import io.github.t3m8ch.springshop.mapToOutDTO
import io.github.t3m8ch.springshop.repositories.CategoryRepository
import io.github.t3m8ch.springshop.services.interfaces.CategoryService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class CategoryServiceImpl(private val categoryRepository: CategoryRepository) : CategoryService {
    override fun getAll(): List<CategoryOutDTO> {
        return categoryRepository.findAllByIsRemoved(false).map { it.mapToOutDTO() }
    }

    override fun getById(id: UUID): CategoryOutDTO {
        val category = categoryRepository.findByIdOrNull(id) ?: throw CategoryNotFoundException(id)
        if (category.isRemoved) throw CategoryIsRemovedException(id)
        return category.mapToOutDTO()
    }

    @Transactional
    override fun create(dto: CreateUpdateCategoryDTO): CategoryOutDTO {
        return categoryRepository.save(CategoryEntity(name = dto.name)).mapToOutDTO()
    }

    override fun updateById(id: UUID, dto: CreateUpdateCategoryDTO): CategoryOutDTO {
        val category = categoryRepository.findByIdOrNull(id) ?: throw CategoryNotFoundException(id)
        if (category.isRemoved) throw CategoryIsRemovedException(id)

        category.name = dto.name
        return categoryRepository.save(category).mapToOutDTO()
    }

    override fun remove(id: UUID): CategoryOutDTO {
        val category = categoryRepository.findByIdOrNull(id) ?: throw CategoryNotFoundException(id)
        if (category.isRemoved) throw CategoryIsRemovedException(id)
        category.isRemoved = true
        return categoryRepository.save(category).mapToOutDTO()
    }

    override fun delete(id: UUID): CategoryOutDTO {
        val category = categoryRepository.findByIdOrNull(id) ?: throw CategoryNotFoundException(id)
        categoryRepository.delete(category)
        return category.mapToOutDTO()
    }

    override fun restoreById(id: UUID): CategoryOutDTO {
        val entity = categoryRepository.findByIdOrNull(id) ?: throw CategoryNotFoundException(id)
        if (!entity.isRemoved) throw CategoryIsNotRemovedException(id)
        entity.isRemoved = false
        return categoryRepository.save(entity).mapToOutDTO()
    }
}
