package io.github.t3m8ch.springshop.e2e.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.t3m8ch.springshop.controllers.ProductController
import io.github.t3m8ch.springshop.dto.CategoryInProductDTO
import io.github.t3m8ch.springshop.dto.CreateUpdateProductDTO
import io.github.t3m8ch.springshop.dto.ProductOutDTO
import io.github.t3m8ch.springshop.exceptions.*
import io.github.t3m8ch.springshop.services.interfaces.ProductService
import isNotEmptyString
import org.hamcrest.CoreMatchers.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.math.BigDecimal
import java.time.ZonedDateTime
import java.util.*

@WebMvcTest(controllers = [ProductController::class])
class ProductControllerTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val objectMapper: ObjectMapper,
) {
    @MockBean
    lateinit var productService: ProductService

    @Test
    fun `test getAll`() {
        val dateString = "2000-01-01T00:00:00Z"
        val date = ZonedDateTime.parse(dateString)

        val uuids = listOf(
            Pair(
                UUID.fromString("28158623-e14a-4b0b-af6f-7b00e82e15d7"),
                UUID.fromString("f6b3ac25-dcf0-43a2-828e-29c82dd62a33")
            ),
            Pair(
                UUID.fromString("87324e08-0594-4370-b993-90cb15e54062"),
                UUID.fromString("1987b1e7-3243-4742-ad33-36b35bc2f52d")
            ),
            Pair(
                UUID.fromString("169c0534-5ce2-442d-90e8-4c291383e173"),
                UUID.fromString("c05e8e1d-16ef-4dea-be47-d42fb3c0d4ec")
            )
        )

        val products = uuids.map { (productId, categoryId) ->
            ProductOutDTO(
                productId,
                date,
                date,
                "Oneplus 9R",
                "descr",
                BigDecimal("25000.00"),
                mapOf("CPU" to "Snapdragon", "RAM" to "6 GB"),
                CategoryInProductDTO(categoryId, "phones")
            )
        }

        val expectedJSON = products.map {
            mapOf(
                "id" to it.id.toString(),
                "createdAt" to dateString,
                "updatedAt" to dateString,
                "title" to it.title,
                "description" to it.description,
                "price" to 25000.00,
                "characteristics" to it.characteristics,
                "category" to mapOf(
                    "id" to it.category.id.toString(),
                    "name" to it.category.name,
                )
            )
        }

        `when`(productService.getAll()).thenReturn(products)

        mockMvc.perform(get("/api/v1/products"))
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(expectedJSON)))
    }

    @Test
    fun `test getAll, if categoryId is define`() {
        val dateString = "2000-01-01T00:00:00Z"
        val date = ZonedDateTime.parse(dateString)

        val categoryId = UUID.fromString("f1508012-a525-42f4-b4b1-f89265f1f0cd")

        val uuids = listOf(
            UUID.fromString("28158623-e14a-4b0b-af6f-7b00e82e15d7"),
            UUID.fromString("87324e08-0594-4370-b993-90cb15e54062"),
            UUID.fromString("169c0534-5ce2-442d-90e8-4c291383e173"),
        )

        val products = uuids.map { productId ->
            ProductOutDTO(
                productId,
                date,
                date,
                "Oneplus 9R",
                "descr",
                BigDecimal("25000.00"),
                mapOf("CPU" to "Snapdragon", "RAM" to "6 GB"),
                CategoryInProductDTO(categoryId, "phones")
            )
        }

        val expectedJSON = products.map {
            mapOf(
                "id" to it.id.toString(),
                "createdAt" to dateString,
                "updatedAt" to dateString,
                "title" to it.title,
                "description" to it.description,
                "price" to 25000.00,
                "characteristics" to it.characteristics,
                "category" to mapOf(
                    "id" to it.category.id.toString(),
                    "name" to it.category.name,
                )
            )
        }

        `when`(productService.getAll(categoryId)).thenReturn(products)

        mockMvc.perform(get("/api/v1/products?categoryId=$categoryId"))
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(expectedJSON)))
    }

    @Test
    fun `test getAll, if categoryId is define, but category not found`() {
        val categoryId = UUID.fromString("f1508012-a525-42f4-b4b1-f89265f1f0cd")

        `when`(productService.getAll(categoryId)).thenThrow(CategoryNotFoundException(categoryId))

        mockMvc.perform(get("/api/v1/products?categoryId=$categoryId"))
            .andExpect(status().isNotFound)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.errorCode", `is`("CATEGORY_NOT_FOUND")))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.description", isNotEmptyString()))
    }

    @Test
    fun `test getAll, if categoryId is define, but category is removed`() {
        val categoryId = UUID.fromString("f1508012-a525-42f4-b4b1-f89265f1f0cd")

        `when`(productService.getAll(categoryId)).thenThrow(CategoryIsRemovedException(categoryId))

        mockMvc.perform(get("/api/v1/products?categoryId=$categoryId"))
            .andExpect(status().isLocked)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.errorCode", `is`("CATEGORY_REMOVED")))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.description", isNotEmptyString()))
    }

    @Test
    fun `test getById`() {
        val productId = UUID.fromString("43f3aef7-5ead-4630-b618-0ccad3b0565e")
        val date = ZonedDateTime.parse("2000-01-01T00:00:00Z")
        val title = "Iphone"
        val description = "descr"
        val price = BigDecimal("67000.00")
        val characteristics = mapOf(
            "CPU" to "A12 Bionic",
            "RAM" to "4GB",
        )
        val category = CategoryInProductDTO(
            id = UUID.fromString("248ceb6c-9c20-48d0-b518-64612c9e8b48"),
            name = "phones"
        )

        val expectedJSON = mapOf(
            "id" to productId.toString(),
            "createdAt" to date,
            "updatedAt" to date,
            "title" to title,
            "description" to description,
            "price" to price,
            "characteristics" to characteristics,
            "category" to mapOf(
                "id" to category.id,
                "name" to category.name,
            )
        )

        val outDTO = ProductOutDTO(
            productId, date, date, title, description, price, characteristics, category
        )
        `when`(productService.getById(productId)).thenReturn(outDTO)

        mockMvc.perform(get("/api/v1/products/$productId"))
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(expectedJSON)))
    }

    @Test
    fun `test getById, if product not found`() {
        val productId = UUID.fromString("43f3aef7-5ead-4630-b618-0ccad3b0565e")

        `when`(productService.getById(productId)).thenThrow(ProductNotFoundException(productId))

        mockMvc.perform(get("/api/v1/products/$productId"))
            .andExpect(status().isNotFound)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.errorCode", `is`("PRODUCT_NOT_FOUND")))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.description", isNotEmptyString()))
    }

    @Test
    fun `test getById, if product is removed`() {
        val productId = UUID.fromString("43f3aef7-5ead-4630-b618-0ccad3b0565e")

        `when`(productService.getById(productId)).thenThrow(ProductIsRemovedException(productId))

        mockMvc.perform(get("/api/v1/products/$productId"))
            .andExpect(status().isLocked)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.errorCode", `is`("PRODUCT_REMOVED")))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.description", isNotEmptyString()))
    }

    @Test
    fun `test create`() {
        val date = ZonedDateTime.parse("2000-01-01T00:00:00Z")

        val productId = UUID.fromString("a0149b84-9cb8-4a98-9429-65f9f82aa78f")
        val categoryId = UUID.fromString("f1508012-a525-42f4-b4b1-f89265f1f0cd")

        val title = "Oneplus 9R"
        val description = "descr"
        val price = BigDecimal("15000.00")
        val characteristics = mapOf("CPU" to "Snapdragon", "RAM" to "6GB")
        val category = CategoryInProductDTO(categoryId, "phones")

        val createDTO = CreateUpdateProductDTO(title, description, price, characteristics, categoryId)

        val outDTO = ProductOutDTO(
            id = productId,
            createdAt = date,
            updatedAt = date,
            title = title,
            description = description,
            price = price,
            characteristics = characteristics,
            category = CategoryInProductDTO(categoryId, "phones"),
        )

        val expectedJSON = mapOf(
            "id" to productId.toString(),
            "createdAt" to date,
            "updatedAt" to date,
            "title" to title,
            "description" to description,
            "price" to price,
            "characteristics" to characteristics,
            "category" to mapOf(
                "id" to category.id,
                "name" to category.name,
            )
        )

        `when`(productService.create(createDTO)).thenReturn(outDTO)

        mockMvc
            .perform(
                post("/api/v1/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createDTO))
            )
            .andExpect(status().isCreated)
            .andExpect(content().json(objectMapper.writeValueAsString(expectedJSON)))
    }

    @Test
    fun `test create, if category not found`() {
        val categoryId = UUID.fromString("f1508012-a525-42f4-b4b1-f89265f1f0cd")

        val title = "Oneplus 9R"
        val description = "descr"
        val price = BigDecimal("15000.00")
        val characteristics = mapOf("CPU" to "Snapdragon", "RAM" to "6GB")

        val createDTO = CreateUpdateProductDTO(title, description, price, characteristics, categoryId)

        `when`(productService.create(createDTO)).thenThrow(CategoryNotFoundException(categoryId))

        mockMvc
            .perform(
                post("/api/v1/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createDTO))
            )
            .andExpect(status().isNotFound)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.errorCode", `is`("CATEGORY_NOT_FOUND")))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.description", isNotEmptyString()))
    }

    @Test
    fun `test create, if category is removed`() {
        val categoryId = UUID.fromString("f1508012-a525-42f4-b4b1-f89265f1f0cd")

        val title = "Oneplus 9R"
        val description = "descr"
        val price = BigDecimal("15000.00")
        val characteristics = mapOf("CPU" to "Snapdragon", "RAM" to "6GB")

        val createDTO = CreateUpdateProductDTO(title, description, price, characteristics, categoryId)

        `when`(productService.create(createDTO)).thenThrow(CategoryIsRemovedException(categoryId))

        mockMvc
            .perform(
                post("/api/v1/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createDTO))
            )
            .andExpect(status().isLocked)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.errorCode", `is`("CATEGORY_REMOVED")))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.description", isNotEmptyString()))
    }

    @Test
    fun `test updateById`() {
        val dateString = "2000-01-01T00:00:00Z"
        val date = ZonedDateTime.parse(dateString)

        val productId = UUID.fromString("a0149b84-9cb8-4a98-9429-65f9f82aa78f")
        val categoryId = UUID.fromString("f1508012-a525-42f4-b4b1-f89265f1f0cd")

        val title = "Oneplus 9R"
        val description = "descr"
        val price = BigDecimal("15000.00")
        val characteristics = mapOf("CPU" to "Snapdragon", "RAM" to "6GB")
        val category = CategoryInProductDTO(categoryId, "phones")

        val updateDTO = CreateUpdateProductDTO(title, description, price, characteristics, categoryId)

        val outDTO = ProductOutDTO(
            id = productId,
            createdAt = date,
            updatedAt = date,
            title = title,
            description = description,
            price = price,
            characteristics = characteristics,
            category = CategoryInProductDTO(categoryId, "phones"),
        )

        val expectedJSON = mapOf(
            "id" to productId.toString(),
            "createdAt" to dateString,
            "updatedAt" to dateString,
            "title" to title,
            "description" to description,
            "price" to 15000.00,
            "characteristics" to characteristics,
            "category" to mapOf(
                "id" to category.id,
                "name" to category.name,
            )
        )

        `when`(productService.updateById(productId, updateDTO)).thenReturn(outDTO)

        mockMvc
            .perform(
                put("/api/v1/products/$productId")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateDTO))
            )
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(expectedJSON)))
    }

    @Test
    fun `test updateById, if product not found`() {
        val productId = UUID.fromString("a0149b84-9cb8-4a98-9429-65f9f82aa78f")
        val categoryId = UUID.fromString("f1508012-a525-42f4-b4b1-f89265f1f0cd")

        val title = "Oneplus 9R"
        val description = "descr"
        val price = BigDecimal("15000.00")
        val characteristics = mapOf("CPU" to "Snapdragon", "RAM" to "6GB")

        val updateDTO = CreateUpdateProductDTO(title, description, price, characteristics, categoryId)

        `when`(productService.updateById(productId, updateDTO)).thenThrow(ProductNotFoundException(categoryId))

        mockMvc
            .perform(
                put("/api/v1/products/$productId")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateDTO))
            )
            .andExpect(status().isNotFound)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.errorCode", `is`("PRODUCT_NOT_FOUND")))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.description", isNotEmptyString()))
    }

    @Test
    fun `test updateById, if product is removed`() {
        val productId = UUID.fromString("a0149b84-9cb8-4a98-9429-65f9f82aa78f")
        val categoryId = UUID.fromString("f1508012-a525-42f4-b4b1-f89265f1f0cd")

        val title = "Oneplus 9R"
        val description = "descr"
        val price = BigDecimal("15000.00")
        val characteristics = mapOf("CPU" to "Snapdragon", "RAM" to "6GB")

        val updateDTO = CreateUpdateProductDTO(title, description, price, characteristics, categoryId)

        `when`(productService.updateById(productId, updateDTO)).thenThrow(ProductIsRemovedException(categoryId))

        mockMvc
            .perform(
                put("/api/v1/products/$productId")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateDTO))
            )
            .andExpect(status().isLocked)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.errorCode", `is`("PRODUCT_REMOVED")))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.description", isNotEmptyString()))
    }

    @Test
    fun `test updateById, if category not found`() {
        val productId = UUID.fromString("a0149b84-9cb8-4a98-9429-65f9f82aa78f")
        val categoryId = UUID.fromString("f1508012-a525-42f4-b4b1-f89265f1f0cd")

        val title = "Oneplus 9R"
        val description = "descr"
        val price = BigDecimal("15000.00")
        val characteristics = mapOf("CPU" to "Snapdragon", "RAM" to "6GB")

        val updateDTO = CreateUpdateProductDTO(title, description, price, characteristics, categoryId)

        `when`(productService.updateById(productId, updateDTO)).thenThrow(CategoryNotFoundException(categoryId))

        mockMvc
            .perform(
                put("/api/v1/products/$productId")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateDTO))
            )
            .andExpect(status().isNotFound)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.errorCode", `is`("CATEGORY_NOT_FOUND")))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.description", isNotEmptyString()))
    }

    @Test
    fun `test updateById, if category is removed`() {
        val productId = UUID.fromString("a0149b84-9cb8-4a98-9429-65f9f82aa78f")
        val categoryId = UUID.fromString("f1508012-a525-42f4-b4b1-f89265f1f0cd")

        val title = "Oneplus 9R"
        val description = "descr"
        val price = BigDecimal("15000.00")
        val characteristics = mapOf("CPU" to "Snapdragon", "RAM" to "6GB")

        val updateDTO = CreateUpdateProductDTO(title, description, price, characteristics, categoryId)

        `when`(productService.updateById(productId, updateDTO)).thenThrow(CategoryIsRemovedException(categoryId))

        mockMvc
            .perform(
                put("/api/v1/products/$productId")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateDTO))
            )
            .andExpect(status().isLocked)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.errorCode", `is`("CATEGORY_REMOVED")))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.description", isNotEmptyString()))
    }

    @Test
    fun `test removeById`() {
        val productId = UUID.fromString("43f3aef7-5ead-4630-b618-0ccad3b0565e")
        val date = ZonedDateTime.parse("2000-01-01T00:00:00Z")
        val title = "Iphone"
        val description = "descr"
        val price = BigDecimal("67000.00")
        val characteristics = mapOf(
            "CPU" to "A12 Bionic",
            "RAM" to "4GB",
        )
        val category = CategoryInProductDTO(
            id = UUID.fromString("248ceb6c-9c20-48d0-b518-64612c9e8b48"),
            name = "phones"
        )

        val expectedJSON = mapOf(
            "id" to productId.toString(),
            "createdAt" to date,
            "updatedAt" to date,
            "title" to title,
            "description" to description,
            "price" to price,
            "characteristics" to characteristics,
            "category" to mapOf(
                "id" to category.id,
                "name" to category.name,
            )
        )

        val outDTO = ProductOutDTO(
            productId, date, date, title, description, price, characteristics, category
        )
        `when`(productService.removeById(productId)).thenReturn(outDTO)

        mockMvc.perform(delete("/api/v1/products/$productId"))
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(expectedJSON)))
    }

    @Test
    fun `test removeById, if product not found`() {
        val productId = UUID.fromString("43f3aef7-5ead-4630-b618-0ccad3b0565e")
        `when`(productService.removeById(productId)).thenThrow(ProductNotFoundException(productId))
        mockMvc.perform(delete("/api/v1/products/$productId"))
            .andExpect(status().isNotFound)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.errorCode", `is`("PRODUCT_NOT_FOUND")))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.description", isNotEmptyString()))
    }

    @Test
    fun `test removeById, if product is removed`() {
        val productId = UUID.fromString("43f3aef7-5ead-4630-b618-0ccad3b0565e")
        `when`(productService.removeById(productId)).thenThrow(ProductIsRemovedException(productId))
        mockMvc.perform(delete("/api/v1/products/$productId"))
            .andExpect(status().isLocked)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.errorCode", `is`("PRODUCT_REMOVED")))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.description", isNotEmptyString()))
    }

    @Test
    fun `test deleteById`() {
        val productId = UUID.fromString("43f3aef7-5ead-4630-b618-0ccad3b0565e")
        val date = ZonedDateTime.parse("2000-01-01T00:00:00Z")
        val title = "Iphone"
        val description = "descr"
        val price = BigDecimal("67000.00")
        val characteristics = mapOf(
            "CPU" to "A12 Bionic",
            "RAM" to "4GB",
        )
        val category = CategoryInProductDTO(
            id = UUID.fromString("248ceb6c-9c20-48d0-b518-64612c9e8b48"),
            name = "phones"
        )

        val expectedJSON = mapOf(
            "id" to productId.toString(),
            "createdAt" to date,
            "updatedAt" to date,
            "title" to title,
            "description" to description,
            "price" to price,
            "characteristics" to characteristics,
            "category" to mapOf(
                "id" to category.id,
                "name" to category.name,
            )
        )

        val outDTO = ProductOutDTO(
            productId, date, date, title, description, price, characteristics, category
        )
        `when`(productService.deleteById(productId)).thenReturn(outDTO)

        mockMvc.perform(delete("/api/v1/products/$productId/hard"))
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(expectedJSON)))
    }

    @Test
    fun `test deleteById, if product not found`() {
        val productId = UUID.fromString("43f3aef7-5ead-4630-b618-0ccad3b0565e")
        `when`(productService.deleteById(productId)).thenThrow(ProductNotFoundException(productId))
        mockMvc.perform(delete("/api/v1/products/$productId/hard"))
            .andExpect(status().isNotFound)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.errorCode", `is`("PRODUCT_NOT_FOUND")))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.description", isNotEmptyString()))
    }

    @Test
    fun `test deleteById, if product is removed`() {
        val productId = UUID.fromString("43f3aef7-5ead-4630-b618-0ccad3b0565e")
        `when`(productService.deleteById(productId)).thenThrow(ProductIsRemovedException(productId))
        mockMvc.perform(delete("/api/v1/products/$productId/hard"))
            .andExpect(status().isLocked)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.errorCode", `is`("PRODUCT_REMOVED")))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.description", isNotEmptyString()))
    }

    @Test
    fun `test restoreById`() {
        val productId = UUID.fromString("43f3aef7-5ead-4630-b618-0ccad3b0565e")
        val date = ZonedDateTime.parse("2000-01-01T00:00:00Z")
        val title = "Iphone"
        val description = "descr"
        val price = BigDecimal("67000.00")
        val characteristics = mapOf(
            "CPU" to "A12 Bionic",
            "RAM" to "4GB",
        )
        val category = CategoryInProductDTO(
            id = UUID.fromString("248ceb6c-9c20-48d0-b518-64612c9e8b48"),
            name = "phones"
        )

        val expectedJSON = mapOf(
            "id" to productId.toString(),
            "createdAt" to date,
            "updatedAt" to date,
            "title" to title,
            "description" to description,
            "price" to price,
            "characteristics" to characteristics,
            "category" to mapOf(
                "id" to category.id,
                "name" to category.name,
            )
        )

        val outDTO = ProductOutDTO(
            productId, date, date, title, description, price, characteristics, category
        )
        `when`(productService.restoreById(productId)).thenReturn(outDTO)

        mockMvc.perform(patch("/api/v1/products/$productId/restore"))
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(expectedJSON)))
    }

    @Test
    fun `test restoreById, if product not found`() {
        val productId = UUID.fromString("43f3aef7-5ead-4630-b618-0ccad3b0565e")
        `when`(productService.restoreById(productId)).thenThrow(ProductNotFoundException(productId))
        mockMvc.perform(patch("/api/v1/products/$productId/restore"))
            .andExpect(status().isNotFound)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.errorCode", `is`("PRODUCT_NOT_FOUND")))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.description", isNotEmptyString()))
    }

    @Test
    fun `test restoreById, if product isn't removed`() {
        val productId = UUID.fromString("43f3aef7-5ead-4630-b618-0ccad3b0565e")
        `when`(productService.restoreById(productId)).thenThrow(ProductIsNotRemovedException(productId))
        mockMvc.perform(patch("/api/v1/products/$productId/restore"))
            .andExpect(status().isConflict)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.errorCode", `is`("PRODUCT_NOT_REMOVED")))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.description", isNotEmptyString()))
    }
}
