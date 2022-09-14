package io.github.susimsek.payment.model

import com.querydsl.core.annotations.QueryEntity
import io.github.susimsek.mscommonweb.mongo.model.audit.AbstractAuditingEntity
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.io.Serializable
import java.util.UUID

@QueryEntity
@Document(collection = "user")
data class User(
    @Id
    var id: String = UUID.randomUUID().toString(),
    var userId: String,
    var balance: Float,
) : AbstractAuditingEntity(), Serializable {

    companion object {
        private const val serialVersionUID = 1L
    }
    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is User) return false
        return id == other.id
    }
}
