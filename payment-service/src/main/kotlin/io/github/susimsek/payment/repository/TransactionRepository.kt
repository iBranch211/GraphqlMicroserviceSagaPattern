package io.github.susimsek.payment.repository

import io.github.susimsek.payment.model.Transaction
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.data.querydsl.ReactiveQuerydslPredicateExecutor
import org.springframework.stereotype.Repository

@Repository
interface TransactionRepository :
    ReactiveMongoRepository<Transaction, String>,
    ReactiveQuerydslPredicateExecutor<Transaction>
