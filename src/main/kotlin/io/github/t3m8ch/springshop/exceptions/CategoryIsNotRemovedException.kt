package io.github.t3m8ch.springshop.exceptions

import io.github.t3m8ch.springshop.dto.APIErrorBody
import org.springframework.http.HttpStatus
import java.util.*

class CategoryIsNotRemovedException(id: UUID) : BaseAPIException(
    HttpStatus.NOT_FOUND,
    APIErrorBody(
        ERROR_CODE,
        "Category with ID = $id isn't removed"
    )
) {
    companion object { const val ERROR_CODE = "CATEGORY_NOT_REMOVED"}
}