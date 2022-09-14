package io.github.susimsek.order.model

import com.querydsl.core.annotations.QueryEntity
import io.github.susimsek.mscommonweb.mongo.model.audit.AbstractAuditingEntity
import io.github.susimsek.mscommonweb.enums.OrderStatus
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.io.Serializable
import java.util.*

@QueryEntity
@Document(collection = "orderPurchase")
data class OrderPurchase(
    @Id
    var id: String = UUID.randomUUID().toString(),
    var userId: String,
    var productId: String,
    var price: Float,
    var status: OrderStatus,
) : AbstractAuditingEntity(), Serializable {

    companion object {
        private const val serialVersionUID = 1L
    }
    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is OrderPurchase) return false
        return id == other.id
    }
}
