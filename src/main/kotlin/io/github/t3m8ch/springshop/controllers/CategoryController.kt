package io.github.t3m8ch.springshop.controllers

import io.github.t3m8ch.springshop.dto.CategoryOutDTO
import io.github.t3m8ch.springshop.dto.CreateUpdateCategoryDTO
import io.github.t3m8ch.springshop.services.interfaces.CategoryService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("api/v1/categories")
class CategoryController(private val categoryService: CategoryService) {
    @GetMapping
    fun getAll() = categoryService.getAll()

    @GetMapping("{id}")
    fun getById(@PathVariable id: UUID): CategoryOutDTO {
        return categoryService.getById(id)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody category: CreateUpdateCategoryDTO): CategoryOutDTO {
        return categoryService.create(category)
    }

    @PutMapping("{id}")
    fun updateById(@PathVariable id: UUID, @RequestBody category: CreateUpdateCategoryDTO): CategoryOutDTO {
        return categoryService.updateById(id, category)
    }

    @DeleteMapping("{id}")
    fun removeById(@PathVariable id: UUID): CategoryOutDTO {
        return categoryService.remove(id)
    }

    @DeleteMapping("{id}/hard")
    fun deleteById(@PathVariable id: UUID): CategoryOutDTO {
        return categoryService.delete(id)
    }

    @PatchMapping("{id}/restore")
    fun restoreById(@PathVariable id: UUID): CategoryOutDTO {
        return categoryService.restoreById(id)
    }
}
