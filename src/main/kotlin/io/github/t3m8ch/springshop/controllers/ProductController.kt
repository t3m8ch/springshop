package io.github.t3m8ch.springshop.controllers

import io.github.t3m8ch.springshop.dto.CreateUpdateProductDTO
import io.github.t3m8ch.springshop.dto.ProductOutDTO
import io.github.t3m8ch.springshop.services.interfaces.ProductService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("api/v1/products")
class ProductController(private val productService: ProductService) {
    @GetMapping
    fun getAll(@RequestParam categoryId: UUID?): List<ProductOutDTO> {
        return productService.getAll(categoryId)
    }

    @GetMapping("{id}")
    fun getById(@PathVariable id: UUID): ProductOutDTO {
        return productService.getById(id)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody product: CreateUpdateProductDTO): ProductOutDTO {
        return productService.create(product)
    }

    @PutMapping("{id}")
    fun updateById(@PathVariable id: UUID, @RequestBody product: CreateUpdateProductDTO): ProductOutDTO {
        return productService.updateById(id, product)
    }

    @DeleteMapping("{id}")
    fun removeById(@PathVariable id: UUID): ProductOutDTO {
        return productService.removeById(id)
    }

    @DeleteMapping("{id}/hard")
    fun deleteById(@PathVariable id: UUID): ProductOutDTO {
        return productService.deleteById(id)
    }

    @PatchMapping("{id}/restore")
    fun restoreById(@PathVariable id: UUID): ProductOutDTO {
        return productService.restoreById(id)
    }
}
