package io.github.t3m8ch.springshop.exceptions.category

import io.github.t3m8ch.springshop.dto.APIErrorBody
import io.github.t3m8ch.springshop.exceptions.BaseAPIException
import org.springframework.http.HttpStatus
import java.util.*

class CategoryNotFoundException(id: UUID) : BaseAPIException(
    HttpStatus.NOT_FOUND,
    APIErrorBody(
        ERROR_CODE,
        "Category with ID = $id not found"
    )
) {
    companion object { const val ERROR_CODE = "CATEGORY_NOT_FOUND" }
}
