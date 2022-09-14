package io.github.susimsek.order.graphql

import io.github.susimsek.order.graphql.input.AddOrderInput
import io.github.susimsek.order.model.OrderPurchase
import io.github.susimsek.order.service.OrderService
import kotlinx.coroutines.flow.toList
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import org.springframework.validation.annotation.Validated

@Controller
@Validated
class OrderController(
    private val orderService: OrderService
) {
    @MutationMapping
    suspend fun createOrder(@Argument input: AddOrderInput): OrderPurchase {
        return orderService.createOrder(input)
    }

    @QueryMapping
    suspend fun orders(): List<OrderPurchase> {
        return orderService.getAllOrders().toList()
    }

    @QueryMapping
    suspend fun order(@Argument id: String): OrderPurchase {
        return orderService.getOrderById(id)
    }
}
