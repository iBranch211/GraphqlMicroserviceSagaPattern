package io.github.susimsek.order.model

import com.querydsl.core.annotations.QueryEntity
import io.github.susimsek.mscommonweb.mongo.model.audit.AbstractAuditingEntity
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.io.Serializable
import java.math.BigDecimal
import java.util.UUID

@QueryEntity
@Document(collection = "product")
data class Product(
    @Id
    var id: String = UUID.randomUUID().toString(),
    var productId: String,
    var price: Float
) : AbstractAuditingEntity(), Serializable {

    companion object {
        private const val serialVersionUID = 1L
    }
    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Product) return false
        return id == other.id
    }
}
