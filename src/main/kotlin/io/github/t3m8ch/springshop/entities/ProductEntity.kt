package io.github.t3m8ch.springshop.entities

import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import io.github.t3m8ch.springshop.CharacteristicsType
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.UpdateTimestamp
import java.math.BigDecimal
import java.time.ZonedDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "product")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType::class)
class ProductEntity(
    @Id
    val id: UUID = UUID.randomUUID(),

    @CreationTimestamp
    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    var createdAt: ZonedDateTime? = null,

    @UpdateTimestamp
    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    var updatedAt: ZonedDateTime? = null,

    @Column(nullable = false)
    var title: String,

    @Column(nullable = false)
    var description: String,

    @Column(nullable = false)
    var price: BigDecimal,

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", nullable = false)
    var characteristics: CharacteristicsType,

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = true)
    var category: CategoryEntity? = null,

    var isRemoved: Boolean = false,
) {
    @PrePersist
    fun defineCreatedAt() {
        val now = ZonedDateTime.now()
        createdAt = now
        updatedAt = now
    }

    @PreUpdate
    fun refreshUpdatedAt() {
        updatedAt = ZonedDateTime.now()
    }
}
