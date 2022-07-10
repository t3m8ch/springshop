package io.github.t3m8ch.springshop.entities

import com.vladmihalcea.hibernate.type.json.JsonBinaryType
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
    @Column(nullable = false)
    val createdAt: ZonedDateTime,

    @UpdateTimestamp
    @Column(nullable = false)
    var updatedAt: ZonedDateTime,

    @Column(nullable = false)
    var title: String,

    @Column(nullable = false)
    var description: String,

    @Column(nullable = false)
    var price: BigDecimal,

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", nullable = false)
    var characteristics: Map<String, String>,

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    var category: CategoryEntity,

    var isRemoved: Boolean = true,
)
