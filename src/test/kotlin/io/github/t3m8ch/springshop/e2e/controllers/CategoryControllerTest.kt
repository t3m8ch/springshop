package io.github.t3m8ch.springshop.e2e.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.t3m8ch.springshop.dto.CategoryOutDTO
import io.github.t3m8ch.springshop.dto.CreateUpdateCategoryDTO
import io.github.t3m8ch.springshop.exceptions.CategoryIsRemovedException
import io.github.t3m8ch.springshop.exceptions.CategoryNotFoundException
import io.github.t3m8ch.springshop.services.interfaces.CategoryService
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matchers.emptyString
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.ZonedDateTime
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude=[
    DataSourceAutoConfiguration::class,
    DataSourceTransactionManagerAutoConfiguration::class,
    HibernateJpaAutoConfiguration::class,
])
class CategoryControllerTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val objectMapper: ObjectMapper,
) {
    @MockBean
    lateinit var categoryService: CategoryService

    @Test
    fun `test getAll`() {
        val date = ZonedDateTime.parse("2000-01-01T00:00:00Z")
        val categoryPairs = listOf(
            Pair(UUID.fromString("f0dcf043-b6d0-4bf9-b99e-f4b0f5fee757"), "phones"),
            Pair(UUID.fromString("fdaf28d6-cd8d-44fd-af8a-fd42bdadd388"), "headphones"),
            Pair(UUID.fromString("5ccd88a4-d22f-4dc6-83db-33247407b4c6"), "food")
        )

        val categories = categoryPairs.map { (id, name) ->
            CategoryOutDTO(
                id = id,
                name = name,
                createdAt = date,
                updatedAt = date,
            )
        }

        val expectedJSON = categories.map {
            mapOf(
                "id" to it.id.toString(),
                "createdAt" to date,
                "updatedAt" to date,
                "name" to it.name,
            )
        }

        `when`(categoryService.getAll()).thenReturn(categories)

        mockMvc.perform(get("/api/v1/categories"))
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(expectedJSON)))
    }

    @Test
    fun `test getById`() {
        val idString = "1343b9dd-3d56-496d-890f-0408f081d13e"
        val dateString = "2000-01-01T00:00:00Z"
        val name = "Phones"

        val expectedJSON = mapOf(
            "id" to idString,
            "createdAt" to dateString,
            "updatedAt" to dateString,
            "name" to name,
        )

        `when`(categoryService.getById(UUID.fromString(idString))).thenReturn(
            CategoryOutDTO(
                id = UUID.fromString(idString),
                createdAt = ZonedDateTime.parse(dateString),
                updatedAt = ZonedDateTime.parse(dateString),
                name = name,
            )
        )

        mockMvc.perform(get("/api/v1/categories/$idString"))
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(expectedJSON)))
    }

    @Test
    fun `test getById, if category not found`() {
        val id = UUID.randomUUID()
        `when`(categoryService.getById(id)).thenThrow(CategoryNotFoundException(id))

        mockMvc.perform(get("/api/v1/categories/$id"))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("\$.errorCode", `is`("CATEGORY_NOT_FOUND")))
            .andExpect(jsonPath("\$.description", isNotEmptyString()))
    }

    @Test
    fun `test getById, if category is removed`() {
        val id = UUID.randomUUID()
        `when`(categoryService.getById(id)).thenThrow(CategoryIsRemovedException(id))

        mockMvc.perform(get("/api/v1/categories/$id"))
            .andExpect(status().isLocked)
            .andExpect(jsonPath("\$.errorCode", `is`("CATEGORY_REMOVED")))
            .andExpect(jsonPath("\$.description", isNotEmptyString()))
    }

    @Test
    fun `test create`() {
        val idString = "1343b9dd-3d56-496d-890f-0408f081d13e"
        val dateString = "2000-01-01T00:00:00Z"
        val name = "Phones"

        val expectedJSON = mapOf(
            "id" to idString,
            "createdAt" to dateString,
            "updatedAt" to dateString,
            "name" to name,
        )

        val outDTO = CategoryOutDTO(
            id = UUID.fromString(idString),
            createdAt = ZonedDateTime.parse(dateString),
            updatedAt = ZonedDateTime.parse(dateString),
            name = name,
        )
        val createDTO = CreateUpdateCategoryDTO(name)
        `when`(categoryService.create(createDTO)).thenReturn(outDTO)

        mockMvc
            .perform(
                post("/api/v1/categories")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createDTO))
            )
            .andExpect(status().isCreated)
            .andExpect(content().json(objectMapper.writeValueAsString(expectedJSON)))
    }

    @Test
    fun `test updateById`() {
        val idString = "1343b9dd-3d56-496d-890f-0408f081d13e"
        val dateString = "2000-01-01T00:00:00Z"
        val name = "Phones"

        val expectedJSON = mapOf(
            "id" to idString,
            "createdAt" to dateString,
            "updatedAt" to dateString,
            "name" to name,
        )

        val outDTO = CategoryOutDTO(
            id = UUID.fromString(idString),
            createdAt = ZonedDateTime.parse(dateString),
            updatedAt = ZonedDateTime.parse(dateString),
            name = name,
        )
        val createDTO = CreateUpdateCategoryDTO(name)
        `when`(categoryService.updateById(UUID.fromString(idString), createDTO)).thenReturn(outDTO)

        mockMvc
            .perform(
                put("/api/v1/categories/$idString")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createDTO))
            )
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(expectedJSON)))
    }

    @Test
    fun `test updateById, if category not found`() {
        val id = UUID.fromString("1343b9dd-3d56-496d-890f-0408f081d13e")
        val name = "Phones"

        val createDTO = CreateUpdateCategoryDTO(name)

        `when`(categoryService.updateById(id, createDTO))
            .thenThrow(CategoryNotFoundException(id))

        mockMvc
            .perform(
                put("/api/v1/categories/$id")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createDTO))
            )
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("\$.errorCode", `is`("CATEGORY_NOT_FOUND")))
            .andExpect(jsonPath("\$.description", isNotEmptyString()))
    }

    @Test
    fun `test updateById, if category is removed`() {
        val id = UUID.fromString("1343b9dd-3d56-496d-890f-0408f081d13e")
        val name = "Phones"

        val createDTO = CreateUpdateCategoryDTO(name)

        `when`(categoryService.updateById(id, createDTO))
            .thenThrow(CategoryIsRemovedException(id))

        mockMvc
            .perform(
                put("/api/v1/categories/$id")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createDTO))
            )
            .andExpect(status().isLocked)
            .andExpect(jsonPath("\$.errorCode", `is`("CATEGORY_REMOVED")))
            .andExpect(jsonPath("\$.description", isNotEmptyString()))
    }

    @Test
    fun `test removeById`() {
        val idString = "1343b9dd-3d56-496d-890f-0408f081d13e"
        val dateString = "2000-01-01T00:00:00Z"
        val name = "Phones"

        val expectedJSON = mapOf(
            "id" to idString,
            "createdAt" to dateString,
            "updatedAt" to dateString,
            "name" to name,
        )

        val outDTO = CategoryOutDTO(
            id = UUID.fromString(idString),
            createdAt = ZonedDateTime.parse(dateString),
            updatedAt = ZonedDateTime.parse(dateString),
            name = name,
        )
        `when`(categoryService.remove(UUID.fromString(idString))).thenReturn(outDTO)

        mockMvc.perform(delete("/api/v1/categories/$idString"))
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(expectedJSON)))
    }

    @Test
    fun `test removeById, if category not found`() {
        val id = UUID.fromString("1343b9dd-3d56-496d-890f-0408f081d13e")

        `when`(categoryService.remove(id)).thenThrow(CategoryNotFoundException(id))

        mockMvc.perform(delete("/api/v1/categories/$id"))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("\$.errorCode", `is`("CATEGORY_NOT_FOUND")))
            .andExpect(jsonPath("\$.description", isNotEmptyString()))
    }

    @Test
    fun `test removeById, if category is removed`() {
        val id = UUID.fromString("1343b9dd-3d56-496d-890f-0408f081d13e")

        `when`(categoryService.remove(id)).thenThrow(CategoryIsRemovedException(id))

        mockMvc.perform(delete("/api/v1/categories/$id"))
            .andExpect(status().isLocked)
            .andExpect(jsonPath("\$.errorCode", `is`("CATEGORY_REMOVED")))
            .andExpect(jsonPath("\$.description", isNotEmptyString()))
    }

    @Test
    fun `test deleteById`() {
        val idString = "1343b9dd-3d56-496d-890f-0408f081d13e"
        val dateString = "2000-01-01T00:00:00Z"
        val name = "Phones"

        val expectedJSON = mapOf(
            "id" to idString,
            "createdAt" to dateString,
            "updatedAt" to dateString,
            "name" to name,
        )

        println(objectMapper.writeValueAsString(expectedJSON))

        val outDTO = CategoryOutDTO(
            id = UUID.fromString(idString),
            createdAt = ZonedDateTime.parse(dateString),
            updatedAt = ZonedDateTime.parse(dateString),
            name = name,
        )
        `when`(categoryService.delete(UUID.fromString(idString))).thenReturn(outDTO)

        mockMvc.perform(delete("/api/v1/categories/$idString/hard"))
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(expectedJSON)))
    }

    @Test
    fun `test deleteById, if category not found`() {
        val id = UUID.fromString("1343b9dd-3d56-496d-890f-0408f081d13e")

        `when`(categoryService.delete(id)).thenThrow(CategoryNotFoundException(id))

        mockMvc.perform(delete("/api/v1/categories/$id/hard"))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("\$.errorCode", `is`("CATEGORY_NOT_FOUND")))
            .andExpect(jsonPath("\$.description", isNotEmptyString()))
    }

    @Test
    fun `test deleteById, if category is removed`() {
        val id = UUID.fromString("1343b9dd-3d56-496d-890f-0408f081d13e")

        `when`(categoryService.delete(id)).thenThrow(CategoryIsRemovedException(id))

        mockMvc.perform(delete("/api/v1/categories/$id/hard"))
            .andExpect(status().isLocked)
            .andExpect(jsonPath("\$.errorCode", `is`("CATEGORY_REMOVED")))
            .andExpect(jsonPath("\$.description", isNotEmptyString()))
    }
}

private fun isNotEmptyString() = `is`(not(emptyString()))
