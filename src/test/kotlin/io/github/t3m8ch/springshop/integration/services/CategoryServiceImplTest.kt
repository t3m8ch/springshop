package io.github.t3m8ch.springshop.integration.services

import io.github.t3m8ch.springshop.ContextInitializer
import io.github.t3m8ch.springshop.dto.CategoryOutDTO
import io.github.t3m8ch.springshop.dto.CreateUpdateCategoryDTO
import io.github.t3m8ch.springshop.entities.CategoryEntity
import io.github.t3m8ch.springshop.entities.ProductEntity
import io.github.t3m8ch.springshop.exceptions.category.CategoryIsRemovedException
import io.github.t3m8ch.springshop.exceptions.category.CategoryNotFoundException
import io.github.t3m8ch.springshop.mapToOutDTO
import io.github.t3m8ch.springshop.services.interfaces.CategoryService
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

private val usedUUIDs = listOf(
    UUID.fromString("0b5c36ce-ebae-45ef-ae0f-2dcaba5909da"),
    UUID.fromString("73c8e1b2-e247-4628-bed7-2dfaba5113e2"),
    UUID.fromString("1dbb1787-dedc-4f42-a7fa-9546a55cfab5"),
    UUID.fromString("94ed320b-98b6-41ae-a550-56358f98dc5b"),
    UUID.fromString("3d8603b2-eaab-4213-9a21-e58b3ca2ceed"),
)

private val unusedUUID = UUID.fromString("1c8a4ec0-88f8-45e8-aa43-bbdc5a3efd66")

private val phones = listOf(
    ProductEntity(
        title = "Iphone",
        price = BigDecimal("60000.00"),
        description = "Cool smartphone",
        characteristics = mapOf("RAM" to "6 GB", "CPU" to "Powerful CPU"),
    ),
    ProductEntity(
        title = "Oneplus",
        price = BigDecimal("30000.00"),
        description = "Cool smartphone",
        characteristics = mapOf("RAM" to "6 GB", "CPU" to "Powerful CPU"),
    ),
    ProductEntity(
        title = "Xiaomi",
        price = BigDecimal("15000.00"),
        description = "Nice smartphone",
        characteristics = mapOf("RAM" to "3 GB", "CPU" to "Average CPU"),
    ),
)

private val headphones = listOf(
    ProductEntity(
        title = "Audio-Technica",
        description = "Studio headphones",
        price = BigDecimal("12000.00"),
        characteristics = mapOf("Gaming" to "No"),
    ),
    ProductEntity(
        title = "Sennheiser",
        description = "Studio headphones",
        price = BigDecimal("15000.00"),
        characteristics = mapOf("Gaming" to "No"),
    ),
    ProductEntity(
        title = "Razer",
        description = "Gaming headphones",
        price = BigDecimal("15000.00"),
        characteristics = mapOf("Gaming" to "Yes"),
    ),
)

private val poorQualityThings = listOf(
    ProductEntity(
        title = "Old apples",
        description = "Old apples",
        price = BigDecimal("40.00"),
        characteristics = mapOf("old" to "Yes"),
    ),
)

private val dangerousThings = listOf(
    ProductEntity(
        title = "Uran",
        description = "Average uran",
        price = BigDecimal("100000000.00"),
        characteristics = mapOf("Radioactive" to "Yes"),
    )
)

private val categories = listOf(
    CategoryEntity(id = usedUUIDs[0], name = "Phones", isRemoved = false, products = phones.toMutableList()),
    CategoryEntity(id = usedUUIDs[1], name = "Headphones", isRemoved = false, products = headphones.toMutableList()),
    CategoryEntity(id = usedUUIDs[2], name = "Watches", isRemoved = false),
    CategoryEntity(
        id = usedUUIDs[3],
        name = "Poor quality things",
        isRemoved = true,
        products = poorQualityThings.toMutableList()
    ),
    CategoryEntity(
        id = usedUUIDs[4],
        name = "Dangerous things",
        isRemoved = true,
        products = dangerousThings.toMutableList()
    ),
)

@SpringBootTest
@AutoConfigureTestEntityManager
@ContextConfiguration(initializers = [ContextInitializer::class])
class CategoryServiceImplTest(
    @Autowired private val service: CategoryService,
    @Autowired private val testEntityManager: TestEntityManager,
) {
    @Test
    @Transactional
    fun `test getAll`() {
        categories.forEach { testEntityManager.persist(it) }

        val expected = categories.slice(0..2).map { it.mapToOutDTO() }.map { it.mapForAssert() }
        val actual = service.getAll().map { it.mapForAssert() }

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    @Transactional
    fun `test getById`() {
        categories.forEach { testEntityManager.persist(it) }

        val expected = categories[0].mapToOutDTO().mapForAssert()
        val actual = service.getById(categories[0].id).mapForAssert()

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    @Transactional
    fun `test getById, if category not found`() {
        categories.forEach { testEntityManager.persist(it) }
        assertThatThrownBy { service.getById(unusedUUID) }.isInstanceOf(CategoryNotFoundException::class.java)
    }

    @Test
    @Transactional
    fun `test getById, if category is removed`() {
        categories.forEach { testEntityManager.persist(it) }
        assertThatThrownBy { service.getById(categories[3].id) }.isInstanceOf(CategoryIsRemovedException::class.java)
    }

    @Test
    @Transactional
    fun `test create`() {
        val categoryFromService = service.create(CreateUpdateCategoryDTO(name = "Fruit"))
        val categoryFromDB = testEntityManager
            .entityManager
            .createQuery("SELECT c FROM CategoryEntity c", CategoryEntity::class.java)
            .resultList[0]

        assertThat(categoryFromDB.name).isEqualTo("Fruit")
        assertThat(categoryFromService.name).isEqualTo("Fruit")
        assertThat(categoryFromService.mapForAssert()).isEqualTo(categoryFromDB.mapToOutDTO().mapForAssert())
    }

    @Test
    @Transactional
    fun `test updateById`() {
        categories.forEach { testEntityManager.persist(it) }

        val id = categories[0].id
        val newName = "Smartphones"

        val categoryFromService = service.updateById(id, CreateUpdateCategoryDTO(newName))
        val categoryFromDB = testEntityManager
            .entityManager
            .find(CategoryEntity::class.java, id)

        assertThat(categoryFromDB.name).isEqualTo(newName)
        assertThat(categoryFromService.name).isEqualTo(newName)
        assertThat(categoryFromService.mapForAssert()).isEqualTo(categoryFromDB.mapToOutDTO().mapForAssert())
    }

    @Test
    fun `test updateById, if category not found`() {
    }

    @Test
    fun `test updateById, if category is removed`() {
    }

    @Test
    fun `test removeById`() {
    }

    @Test
    fun `test removeById, if category not found`() {
    }

    @Test
    fun `test removeById, if category is removed`() {
    }

    @Test
    fun `test deleteById`() {
    }

    @Test
    fun `test deleteById, if category not found`() {
    }

    @Test
    fun `test restoreById`() {
    }

    @Test
    fun `test restoreById, if category not found`() {
    }

    @Test
    fun `test restoreById, if category isn't removed`() {
    }
}

fun CategoryOutDTO.mapForAssert() = "$id $name ${createdAt.formatForAssert()} ${updatedAt.formatForAssert()}"

fun ZonedDateTime.formatForAssert(): String = DateTimeFormatter
    .ofPattern("dd/MM/yyyy hh:mm")
    .format(this)
