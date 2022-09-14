package io.github.susimsek.payment.model

import com.querydsl.core.annotations.QueryEntity
import io.github.susimsek.mscommonweb.mongo.model.audit.AbstractAuditingEntity
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.io.Serializable

@QueryEntity
@Document(collection = "payment")
data class Payment(
    @Id
    var id: String,
    var amount: Float,
) : AbstractAuditingEntity(), Serializable {

    companion object {
        private const val serialVersionUID = 1L
    }
    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Payment) return false
        return id == other.id
    }
}
