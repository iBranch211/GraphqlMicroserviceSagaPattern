package io.github.susimsek.order.repository

import io.github.susimsek.order.model.OrderPurchase
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.data.querydsl.ReactiveQuerydslPredicateExecutor
import org.springframework.stereotype.Repository

@Repository
interface OrderPurchaseRepository :
    ReactiveMongoRepository<OrderPurchase, String>,
    ReactiveQuerydslPredicateExecutor<OrderPurchase>
