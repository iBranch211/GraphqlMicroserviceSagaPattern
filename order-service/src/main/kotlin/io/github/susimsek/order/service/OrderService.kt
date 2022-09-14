package io.github.susimsek.order.service

import io.github.susimsek.mscommonweb.graphql.exception.ResourceNotFoundException
import io.github.susimsek.mscommonweb.enums.OrderStatus
import io.github.susimsek.order.graphql.input.AddOrderInput
import io.github.susimsek.order.model.OrderPurchase
import io.github.susimsek.order.producer.OrderPurchaseProducer
import io.github.susimsek.order.repository.OrderPurchaseRepository
import io.github.susimsek.order.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class OrderService(
    private val orderPurchaseRepository: OrderPurchaseRepository,
    private val orderPurchaseProducer: OrderPurchaseProducer,
    private val productRepository: ProductRepository,
) {

    suspend fun createOrder(order: AddOrderInput): OrderPurchase {
        val orderPurchase = getOrderPurchase(order)
        val savedOrderPurchase = orderPurchaseRepository.save(orderPurchase).awaitSingle()
        orderPurchaseProducer.produce(savedOrderPurchase)
        return orderPurchase
    }

    fun getAllOrders(): Flow<OrderPurchase> {
        return orderPurchaseRepository.findAll().asFlow()
    }

    suspend fun getOrderById(id: String): OrderPurchase {
        return orderPurchaseRepository.findById(id)
            .switchIfEmpty(Mono.error((ResourceNotFoundException("Order", "id", id))))
            .awaitSingle()
    }

    suspend fun getOrderPurchase(order: AddOrderInput): OrderPurchase {
        return productRepository.findById(order.productId)
            .switchIfEmpty(Mono.error((ResourceNotFoundException("Product", "id", order.productId))))
            .map { product ->
                OrderPurchase(
                productId = order.productId,
                userId = order.userId,
                price = product.price,
                status = OrderStatus.CREATED
                )
            }
            .awaitSingle()
    }
}
