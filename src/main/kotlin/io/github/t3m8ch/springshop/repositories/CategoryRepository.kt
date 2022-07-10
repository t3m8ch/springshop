package io.github.t3m8ch.springshop.repositories

import io.github.t3m8ch.springshop.entities.CategoryEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface CategoryRepository : JpaRepository<CategoryEntity, UUID> {
    fun findAllByIsRemoved(removed: Boolean): List<CategoryEntity>
}
