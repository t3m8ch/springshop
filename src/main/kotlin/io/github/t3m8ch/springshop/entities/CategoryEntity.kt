package io.github.t3m8ch.springshop.entities

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.ZonedDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "category")
class CategoryEntity(
    @Id
    val id: UUID = UUID.randomUUID(),

    @CreationTimestamp
    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    var createdAt: ZonedDateTime? = null,

    @UpdateTimestamp
    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    var updatedAt: ZonedDateTime? = null,

    @Column(name = "name_", nullable = false)
    var name: String,

    @OneToMany(mappedBy = "category", cascade=[CascadeType.PERSIST])
    var products: MutableList<ProductEntity> = mutableListOf(),

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
