package io.github.t3m8ch.springshop.services.impl

import io.github.t3m8ch.springshop.dto.CreateUpdateProductDTO
import io.github.t3m8ch.springshop.dto.ProductOutDTO
import io.github.t3m8ch.springshop.services.interfaces.ProductService
import org.springframework.stereotype.Service
import java.util.*

@Service
class ProductServiceImpl : ProductService {
    override fun getAll(categoryId: UUID?): List<ProductOutDTO> {
        TODO("Not yet implemented")
    }

    override fun getById(id: UUID): ProductOutDTO {
        TODO("Not yet implemented")
    }

    override fun create(dto: CreateUpdateProductDTO): ProductOutDTO {
        TODO("Not yet implemented")
    }

    override fun updateById(id: UUID, dto: CreateUpdateProductDTO): ProductOutDTO {
        TODO("Not yet implemented")
    }

    override fun removeById(id: UUID): ProductOutDTO {
        TODO("Not yet implemented")
    }

    override fun deleteById(id: UUID): ProductOutDTO {
        TODO("Not yet implemented")
    }

    override fun restoreById(id: UUID): ProductOutDTO {
        TODO("Not yet implemented")
    }
}